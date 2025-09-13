package ir.sharif.simplenote.feature.note.domain.repository

import ir.sharif.simplenote.feature.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun   observeNotes(): Flow<List<Note>>
    suspend fun getNotes(): List<Note>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note): Int        // returns new id
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun searchNotes(query: String): List<Note>
}
