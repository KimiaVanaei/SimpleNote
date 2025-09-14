package ir.sharif.simplenote.feature.note.data.repository

import ir.sharif.simplenote.feature.note.data.local.NoteDao
import ir.sharif.simplenote.feature.note.data.mapper.toDomain
import ir.sharif.simplenote.feature.note.data.mapper.toDto
import ir.sharif.simplenote.feature.note.data.mapper.toEntity
import ir.sharif.simplenote.feature.note.data.remote.NoteApi
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val dao: NoteDao,
    private val api: NoteApi
) : NotesRepository {

    override fun observeNotes(username: String): Flow<List<Note>> =
        dao.observeAll(username).map { list -> list.map { it.toDomain() } }

    override suspend fun getNotes(username: String): List<Note> {
        val local = dao.getAllNotes(username).map { it.toDomain() }
        return try {
            val remote = api.getNotes().results.map { it.toEntity(username) }
            dao.insertAll(remote)
            dao.getAllNotes(username).map { it.toDomain() }
        } catch (e: Exception) {
            local
        }
    }

    override suspend fun getNoteById(id: Int, username: String): Note? =
        dao.getById(id, username)?.toDomain()


    override suspend fun addNote(username: String, note: Note): Int {
        val localId = dao.insert(note.copy(isSynced = false).toEntity(username)).toInt()
        try {
            val created = api.createNote(note.toDto())
            dao.update(created.toEntity(username).copy(localId = localId)) // حفظ localId
            return created.id ?: localId
        } catch (e: Exception) {
            return localId
        }
    }

    override suspend fun updateNote(username: String, note: Note) {
        dao.update(note.copy(isSynced = false).toEntity(username))
        try {
            note.serverId?.let { sid ->
                val updated = api.updateNote(sid, note.toDto())
                dao.update(updated.toEntity(username).copy(localId = note.localId, isSynced = true))
            }
        } catch (_: Exception) {
            // log
        }
    }

    override suspend fun deleteNote(username: String, note: Note) {
        dao.delete(note.toEntity(username))
        try {
            note.serverId?.let { sid ->
                api.deleteNote(sid)
            }
        } catch (_: Exception) {
            // some Log
        }
    }

    override suspend fun searchNotes(username: String, query: String): List<Note> =
        dao.searchNotes(query, username).map { it.toDomain() }

    override suspend fun syncNotes(username: String) {
        val unsynced = dao.getAllNotes(username).filter { !it.isSynced }
        unsynced.forEach {
            try {
                val created = if (it.serverId == null) {
                    api.createNote(it.toDomain().toDto())
                } else {
                    api.updateNote(it.serverId, it.toDomain().toDto())
                }
                dao.update(created.toEntity(username).copy(localId = it.localId, isSynced = true))
            } catch (_: Exception) {
                // log
            }
        }
        try {
            val remote = api.getNotes().results.map { it.toEntity(username) }
            dao.insertAll(remote)
        } catch (_: Exception) {
            // log
        }
    }
}