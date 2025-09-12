package ir.sharif.simplenote.feature.note.domain.usecase

import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository

class AddNoteUseCase(private val repo: NotesRepository) {
    suspend operator fun invoke(note: Note) {
        repo.addNote(note)
    }
}

