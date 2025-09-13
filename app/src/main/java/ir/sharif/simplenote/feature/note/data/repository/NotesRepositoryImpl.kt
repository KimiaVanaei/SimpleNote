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

    override fun observeNotes(): Flow<List<Note>> =
        dao.observeAll().map { it.map { e -> e.toDomain() } }

    override suspend fun getNotes(): List<Note> =
        dao.getAllNotes().map { it.toDomain() }

    override suspend fun getNoteById(id: Int): Note? =
        dao.getById(id)?.toDomain()

    override suspend fun addNote(note: Note): Int =
        dao.insert(note.toEntity()).toInt()

    override suspend fun updateNote(note: Note) {
        dao.update(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.delete(note.toEntity())
    }

    override suspend fun searchNotes(query: String): List<Note> =
        dao.searchNotes(query).map { it.toDomain() }
}
