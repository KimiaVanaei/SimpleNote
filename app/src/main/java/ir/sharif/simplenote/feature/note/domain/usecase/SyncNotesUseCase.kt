package ir.sharif.simplenote.feature.note.domain.usecase

import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository

class SyncNotesUseCase(
    private val repo: NotesRepository
) {
    suspend operator fun invoke(username: String) {
        repo.syncNotes(username)
    }
}

