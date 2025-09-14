// File name suggestion: feature/note/paging/
// Drop this single file into your project and wire the TODOs where noted.

package ir.sharif.simplenote.feature.note.paging

import androidx.paging.*
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class RemoteNote(
    val id: Int,
    val title: String,
    val description: String
)

data class NotesPage(
    val results: List<RemoteNote>,
    val next: String?,        // null => no more
    val previous: String?     // null => no more
)

interface NotesRemoteDataSource {
    suspend fun list(page: Int, pageSize: Int): NotesPage
    suspend fun create(title: String, description: String): RemoteNote
    suspend fun update(serverId: Int, title: String, description: String): RemoteNote
}

// -----------------------------------
// 2) RemoteKeys table (Room)
//    Put these exactly as-is; then add the migration below to your DB.
// -----------------------------------

@Entity(tableName = "note_remote_keys")
data class NoteRemoteKeysEntity(
    @PrimaryKey val noteId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)

@Dao
interface NoteRemoteKeysDao {
    @Query("SELECT * FROM note_remote_keys WHERE noteId = :noteId")
    suspend fun remoteKeys(noteId: Int): NoteRemoteKeysEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<NoteRemoteKeysEntity>)

    @Query("DELETE FROM note_remote_keys")
    suspend fun clearAll()
}

/**
 * Room migration for creating remote keys table.
 * Call this when building your DB: .addMigrations(migrationAddNoteRemoteKeys(FROM, TO))
 */
fun migrationAddNoteRemoteKeys(fromVersion: Int, toVersion: Int) = object : Migration(fromVersion, toVersion) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS note_remote_keys(
                noteId INTEGER NOT NULL PRIMARY KEY,
                prevKey INTEGER,
                nextKey INTEGER
            )
            """.trimIndent()
        )
    }
}

/**
 * Generic Notes RemoteMediator.
 *
 * You provide:
 * - keysDao: your NoteRemoteKeysDao
 * - insertNotes: suspending function to upsert a batch of NoteEntity (inside transaction)
 * - pagingSourceFactory: returns PagingSource<Int, NoteEntity> (from your DAO)
 * - tx: suspending transaction runner (e.g., { block -> db.withTransaction { block() } })
 * - remote: your NotesRemoteDataSource
 * - mapper: map RemoteNote -> NoteEntity (set isSynced = true here if you track it)
 * - idOf: returns the stable key for a NoteEntity (prefer serverId if you have it; else local id)
 *
 * This avoids forcing changes to your existing DAO signatures.
 */
@OptIn(ExperimentalPagingApi::class)
class NotesRemoteMediator<NoteEntity : Any>(
    private val keysDao: NoteRemoteKeysDao,
    private val insertNotes: suspend (List<NoteEntity>) -> Unit,
    private val pagingSourceFactory: () -> PagingSource<Int, NoteEntity>,
    private val tx: suspend (suspend () -> Unit) -> Unit,
    private val remote: NotesRemoteDataSource,
    private val pageSize: Int,
    private val mapper: (RemoteNote) -> NoteEntity,

private val idOf: (NoteEntity) -> Int,
    private val clearOnRefresh: Boolean = false
) : RemoteMediator<Int, NoteEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoteEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    val first = state.firstItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    val key = keysDao.remoteKeys(idOf(first))?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    key
                }
                LoadType.APPEND -> {
                    val last = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    val key = keysDao.remoteKeys(idOf(last))?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    key
                }
            }

            val remotePage = remote.list(
                page = page,
                pageSize = state.config.pageSize.takeIf { it > 0 } ?: pageSize
            )
            val endOfPagination = remotePage.next == null
            val entities = remotePage.results.map(mapper)

            tx {
                if (loadType == LoadType.REFRESH) {
                    keysDao.clearAll()
                    if (clearOnRefresh) {
                        // If you need to clear your notes table on refresh, do it here.
                        // e.g., noteDao.clearAll()
                    }
                }

                // Upsert notes
                insertNotes(entities)

                // Keys
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = entities.map { e ->
                    NoteRemoteKeysEntity(
                        noteId = idOf(e),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                keysDao.insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}

private fun <T : Any> PagingState<Int, T>.firstItemOrNull(): T? =
    pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()

private fun <T : Any> PagingState<Int, T>.lastItemOrNull(): T? =
    pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()

// ----------------------------------------------------------------------
// 4) Paging Flow builder (returns Flow<PagingData<Domain>> for your UI)
// ----------------------------------------------------------------------

/**
 * Build a Flow<PagingData<Domain>> without touching your existing files.
 *
 * You pass:
 *  - keysDao: NoteRemoteKeysDao
 *  - insertNotes: batch upsert into your notes table
 *  - pagingSourceFactory: returns PagingSource<Int, NoteEntity>
 *  - tx: transaction runner (e.g. { b -> db.withTransaction { b() } })
 *  - remote: NotesRemoteDataSource
 *  - mapRemoteToEntity: RemoteNote -> NoteEntity
 *  - mapEntityToDomain: NoteEntity -> Domain (e.g., your Note domain model)
 *  - idOf: stable id for keys (prefer serverId, else local id)
 */
@OptIn(ExperimentalPagingApi::class)
fun <NoteEntity : Any, Domain : Any> buildPagedNotesFlow(
    keysDao: NoteRemoteKeysDao,
    insertNotes: suspend (List<NoteEntity>) -> Unit,
    pagingSourceFactory: () -> PagingSource<Int, NoteEntity>,
    tx: suspend (suspend () -> Unit) -> Unit,
    remote: NotesRemoteDataSource,
    pageSize: Int = 20,
    mapRemoteToEntity: (RemoteNote) -> NoteEntity,
    mapEntityToDomain: (NoteEntity) -> Domain,
    idOf: (NoteEntity) -> Int,
    clearOnRefresh: Boolean = false
): Flow<PagingData<Domain>> {
    val mediator = NotesRemoteMediator(
        keysDao = keysDao,
        insertNotes = insertNotes,
        pagingSourceFactory = pagingSourceFactory,
        tx = tx,
        remote = remote,

pageSize = pageSize,
        mapper = mapRemoteToEntity,
        idOf = idOf,
        clearOnRefresh = clearOnRefresh
    )

    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            prefetchDistance = pageSize / 2,
            enablePlaceholders = false
        ),
        remoteMediator = mediator,
        pagingSourceFactory = pagingSourceFactory
    ).flow.map { paging -> paging.map(mapEntityToDomain) }
}

// ----------------------------------------------------------------------
// 5) Usage (how/where to wire; keep as reference)
// ----------------------------------------------------------------------
/*
=========================================================
Step A) Add RemoteKeys table to your DB:
---------------------------------------------------------
- Include NoteRemoteKeysEntity and NoteRemoteKeysDao from this file.
- Add an abstract fun noteRemoteKeysDao(): NoteRemoteKeysDao to your AppDatabase.
- Bump DB version and add migration:
    .addMigrations(migrationAddNoteRemoteKeys(FROM_VERSION, TO_VERSION))

=========================================================
Step B) DAO: provide a PagingSource
---------------------------------------------------------
In your NoteDao:
    @Query("SELECT * FROM notes ORDER BY last_edited DESC")
    fun pagingSource(): PagingSource<Int, NoteEntity>

=========================================================
Step C) Provide dependencies where you build the flow:
---------------------------------------------------------
val flow: Flow<PagingData<Note>> = buildPagedNotesFlow(
    keysDao = db.noteRemoteKeysDao(),
    insertNotes = { batch -> noteDao.insertAll(batch) }, // or upsert loop
    pagingSourceFactory = { noteDao.pagingSource() },
    tx = { block -> db.withTransaction { block() } },
    remote = notesRemoteDataSource,           // your Retrofit/Ktor impl
    pageSize = 20,
    mapRemoteToEntity = { rn ->
        // Map RemoteNote -> NoteEntity
        NoteEntity(
            id = 0,                           // let Room autogenerate if local pk != server id
            serverId = rn.id,                 // if you track serverId
            title = rn.title,
            content = rn.description,
            lastEdited = System.currentTimeMillis(),
            isSynced = true
        )
    },
    mapEntityToDomain = { e ->
        // Map NoteEntity -> Note (domain)
        Note(
            id = e.id,
            serverId = e.serverId,
            title = e.title,
            content = e.content,
            lastEdited = e.lastEdited,
            isSynced = e.isSynced
        )
    },
    idOf = { e -> e.serverId ?: e.id },      // stable key for remote keys
    clearOnRefresh = false
)

=========================================================
Step D) ViewModel/UI (Compose):
---------------------------------------------------------
// In VM:
val paged = flow.cachedIn(viewModelScope)

// In HomeScreen:
val items = vm.paged.collectAsLazyPagingItems()
LazyColumn { items(items.itemCount) { idx -> items[idx]?.let { note -> NoteCard(note) } } }
=========================================================
*/
