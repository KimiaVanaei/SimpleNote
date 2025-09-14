package ir.sharif.simplenote.feature.note.data.repository

import ir.sharif.simplenote.feature.note.data.local.NoteDao
import ir.sharif.simplenote.feature.note.data.mapper.toDomain
import ir.sharif.simplenote.feature.note.data.mapper.toEntity
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
            dao.update(created.toEntity(username))
            return created.id ?: localId
        } catch (e: Exception) {
            return localId
        }
    }

    override suspend fun updateNote(username: String, note: Note) {
        dao.update(note.copy(isSynced = false).toEntity(username))
        try {
            val updated = api.updateNote(note.id, note.toDto())
            dao.update(updated.toEntity(username).copy(isSynced = true))
        } catch (_: Exception) {
            // some Log
        }
    }

    override suspend fun deleteNote(username: String, note: Note) {
        dao.delete(note.toEntity(username))
        try {
            if (note.id != 0) {
                api.deleteNote(note.id)
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
                val created = if (it.id == 0) {
                    api.createNote(it.toDomain().toDto())
                } else {
                    api.updateNote(it.id, it.toDomain().toDto())
                }
                dao.update(created.toEntity(username).copy(isSynced = true))
            } catch (_: Exception) {
                // some Log
            }
        }
        try {
            val remote = api.getNotes().results.map { it.toEntity(username) }
            dao.insertAll(remote)
        } catch (_: Exception) {
            // some Log
        }
    }
}
