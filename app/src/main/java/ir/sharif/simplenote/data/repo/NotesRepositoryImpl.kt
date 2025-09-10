package ir.sharif.simplenote.data.repo

import ir.sharif.simplenote.data.local.dao.NoteDao
import ir.sharif.simplenote.data.local.entity.NoteEntity
import ir.sharif.simplenote.data.mappers.toDomain
import ir.sharif.simplenote.data.mappers.toEntity
import ir.sharif.simplenote.data.remote.NotesService
import ir.sharif.simplenote.data.remote.dto.NoteRequestDTO
import ir.sharif.simplenote.domain.model.Note
import ir.sharif.simplenote.domain.repo.NotesRepository
import ir.sharif.simplenote.domain.repo.SyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class NotesRepositoryImpl(
    private val dao: NoteDao,
    private val service: NotesService,
    private val lastSyncStore: LastSyncStore
) : NotesRepository {
    override fun observeNotes(query: String?): Flow<List<Note>> =
        dao.observeAll(q = "%${query?.trim() ?: ""}%").map { list -> list.map { it.toDomain() } }


    override suspend fun get(localId: String): Note? = dao.getByLocalId(localId)?.toDomain()


    override suspend fun create(title: String, description: String): String {
        require(title.isNotBlank() && title.length <= 100) { "Title must be 1..100 chars" }
        require(description.isNotBlank()) { "Description must not be blank" }
        val id = UUID.randomUUID().toString()
        val entity = NoteEntity(
            localId = id,
            title = title,
            description = description,
            isDirty = true,
            isDeleted = false,
            lastModifiedLocal = System.currentTimeMillis()
        )
        dao.upsert(entity)
        return id
    }

    override suspend fun update(localId: String, title: String, description: String) {
        require(title.isNotBlank() && title.length <= 100)
        require(description.isNotBlank())
        val current = dao.getByLocalId(localId) ?: return
        dao.upsert(current.copy(
            title = title,
            description = description,
            isDirty = true,
            lastModifiedLocal = System.currentTimeMillis()
        ))
    }

    override suspend fun delete(localId: String) {
        val current = dao.getByLocalId(localId) ?: return
        if (current.serverId == null) {
// Never synced to server, safe to hard delete
            dao.hardDelete(localId)
        } else {
            dao.softDelete(localId)
        }
    }

    override suspend fun syncOnce(): SyncResult {
        val errors = mutableListOf<String>()
        var pushed = 0
        var pulled = 0


// 1) PUSH local changes
        val dirty = dao.getDirty()
        for (e in dirty) {
            try {
                if (e.isDeleted) {
                    e.serverId?.let { service.delete(it) }
                    dao.hardDelete(e.localId) // remove locally
                } else if (e.serverId == null) {
                    val created = service.create(NoteRequestDTO(e.title, e.description))
                    dao.attachServerFields(
                        localId = e.localId,
                        serverId = created.id,
                        createdAt = ir.sharif.simplenote.data.mappers.parseIso(created.created_at),
                        updatedAt = ir.sharif.simplenote.data.mappers.parseIso(created.updated_at),
                        creatorName = created.creator_name,
                        creatorUsername = created.creator_username
                    )
                    dao.markSynced(e.localId)
                } else {
                    val updated = service.patch(e.serverId, NoteRequestDTO(e.title, e.description))
                    dao.attachServerFields(
                        localId = e.localId,
                        serverId = updated.id,
                        createdAt = ir.sharif.simplenote.data.mappers.parseIso(updated.created_at),
                        updatedAt = ir.sharif.simplenote.data.mappers.parseIso(updated.updated_at),
                        creatorName = updated.creator_name,
                        creatorUsername = updated.creator_username
                    )
                    dao.markSynced(e.localId)
                }
                pushed++
            } catch (t: Throwable) {
                errors += "Push failed for ${e.localId}: ${t.message}"
                dao.setSyncError(e.localId, t.message)
            }
        }
        dao.purgeLocallyDeletedSynced()

        // 2) PULL server changes (delta by updated__gte)
        val since = lastSyncStore.getLastSuccessfulPull()
        var page: Int? = null
        do {
            val pageResp = service.filterNotes(
                updatedGte = since,
                page = page
            )
            for (n in pageResp.results) {
                val existing = n.id.let { dao.getByServerId(it) }
                val entityFromServer = n.toEntity(existing?.localId)
// Conflict policy: last-write-wins
                if (existing == null || !existing.isDirty || (existing.updatedAt ?: 0L) <= (entityFromServer.updatedAt ?: Long.MAX_VALUE)) {
                    dao.upsert(entityFromServer)
                }
                pulled++
            }
            page = nextPage(pageResp.next)
        } while (page != null)
        lastSyncStore.setLastSuccessfulPullToNow()


        return SyncResult(pushed = pushed, pulled = pulled, errors = errors)
    }


    private fun nextPage(nextUrl: String?): Int? {
        if (nextUrl.isNullOrBlank()) return null
        return runCatching {
            val idx = nextUrl.indexOf("page=")
            if (idx == -1) null else nextUrl.substring(idx + 5).takeWhile { it.isDigit() }.toInt()
        }.getOrNull()
    }
}