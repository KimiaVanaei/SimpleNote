package ir.sharif.simplenote.feature.note.domain.repository

import ir.sharif.simplenote.feature.note.domain.model.Note

interface NotesRepository {
    suspend fun getNotes(): List<Note>
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun searchNotes(query: String): List<Note>
}

