package ir.sharif.simplenote.feature.note.data.repository

import ir.sharif.simplenote.feature.note.data.local.NoteDao
import ir.sharif.simplenote.feature.note.data.mapper.toDomain
import ir.sharif.simplenote.feature.note.data.mapper.toEntity
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository

class NotesRepositoryImpl(
    private val dao: NoteDao
) : NotesRepository {

    override suspend fun getNotes(): List<Note> {
        return dao.getAllNotes().map { it.toDomain() }
    }

    override suspend fun addNote(note: Note) {
        dao.insert(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        dao.update(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.delete(note.toEntity())
    }

    override suspend fun searchNotes(query: String): List<Note> {
        return dao.searchNotes(query).map { it.toDomain() }
    }
}