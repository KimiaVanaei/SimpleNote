package ir.sharif.simplenote.feature.note.domain.repository

import ir.sharif.simplenote.feature.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun observeNotes(username: String): Flow<List<Note>>
    suspend fun getNotes(username: String): List<Note>
    suspend fun getNoteById(id: Int, username: String): Note?
    suspend fun addNote(username: String, note: Note): Int   // returns new id
    suspend fun updateNote(username: String, note: Note)
    suspend fun deleteNote(username: String, note: Note)
    suspend fun searchNotes(username: String, query: String): List<Note>
}
