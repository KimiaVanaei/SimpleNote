package ir.sharif.simplenote.feature.note.domain.usecase

import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository

class SearchNotesUseCase(private val repo: NotesRepository) {
    suspend operator fun invoke(query: String): List<Note> {
        return repo.searchNotes(query)
    }
}
