package ir.sharif.simplenote.data.local.dao

import androidx.room.*
import ir.sharif.simplenote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    // Show non-deleted notes; put NULL updatedAt at the end, then newest first
    @Query(
        "SELECT * FROM notes " +
                "WHERE isDeleted = 0 AND (title LIKE :q OR description LIKE :q) " +
                "ORDER BY (updatedAt IS NULL) ASC, updatedAt DESC, lastModifiedLocal DESC"
    )
    fun observeAll(q: String = "%%"): Flow<List<NoteEntity>>


    @Query("SELECT * FROM notes WHERE localId = :localId LIMIT 1")
    suspend fun getByLocalId(localId: String): NoteEntity?


    @Query("SELECT * FROM notes WHERE serverId = :serverId LIMIT 1")
    suspend fun getByServerId(serverId: Int): NoteEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: NoteEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<NoteEntity>)


    @Query("UPDATE notes SET isDeleted = 1, isDirty = 1, lastModifiedLocal = :now WHERE localId = :localId")
    suspend fun softDelete(localId: String, now: Long = System.currentTimeMillis())


    @Query("DELETE FROM notes WHERE localId = :localId")
    suspend fun hardDelete(localId: String)


    @Query("SELECT * FROM notes WHERE isDirty = 1 ORDER BY lastModifiedLocal ASC")
    suspend fun getDirty(): List<NoteEntity>


    @Query("UPDATE notes SET syncError = :error WHERE localId = :localId")
    suspend fun setSyncError(localId: String, error: String?)


    @Query("UPDATE notes SET isDirty = 0, syncError = NULL WHERE localId = :localId")
    suspend fun markSynced(localId: String)


    @Query("UPDATE notes SET serverId = :serverId, createdAt = :createdAt, updatedAt = :updatedAt, creatorName = :creatorName, creatorUsername = :creatorUsername WHERE localId = :localId")
    suspend fun attachServerFields(
        localId: String,
        serverId: Int?,
        createdAt: Long?,
        updatedAt: Long?,
        creatorName: String?,
        creatorUsername: String?
    )


    @Query("DELETE FROM notes WHERE isDeleted = 1 AND isDirty = 0")
    suspend fun purgeLocallyDeletedSynced()
}