package ir.sharif.simplenote.feature.note.domain.usecase

import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository

class AddNoteUseCase(private val repo: NotesRepository) {
    /** Returns the newly inserted note id. */
    suspend operator fun invoke(username: String, note: Note): Int {
        return repo.addNote(username, note)
    }
}
