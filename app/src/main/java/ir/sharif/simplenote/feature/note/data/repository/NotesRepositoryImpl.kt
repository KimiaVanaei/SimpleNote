package ir.sharif.simplenote.feature.note.data.repository

import ir.sharif.simplenote.feature.note.data.local.NoteDao
import ir.sharif.simplenote.feature.note.data.mapper.toDomain
import ir.sharif.simplenote.feature.note.data.mapper.toEntity
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class NotesRepositoryImpl(
    private val dao: NoteDao
) : NotesRepository {

    override fun observeNotes(username: String): Flow<List<Note>> =
        dao.observeAll(username).map { list -> list.map { it.toDomain() } }

    override suspend fun getNotes(username: String): List<Note> =
        dao.getAllNotes(username).map { it.toDomain() }

    override suspend fun getNoteById(id: Int, username: String): Note? =
        dao.getById(id, username)?.toDomain()

    override suspend fun addNote(username: String, note: Note): Int =
        dao.insert(note.toEntity(username)).toInt()

    override suspend fun updateNote(username: String, note: Note) {
        dao.update(note.toEntity(username))
    }

    override suspend fun deleteNote(username: String, note: Note) {
        dao.delete(note.toEntity(username))
    }

    override suspend fun searchNotes(username: String, query: String): List<Note> =
        dao.searchNotes(query, username).map { it.toDomain() }
}
