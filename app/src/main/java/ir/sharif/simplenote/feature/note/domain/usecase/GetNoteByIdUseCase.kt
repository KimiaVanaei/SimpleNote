package ir.sharif.simplenote.feature.note.domain.usecase
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository

class GetNoteByIdUseCase(private val repo: NotesRepository) {
    suspend operator fun invoke(id: Int): Note? = repo.getNoteById(id)
}
