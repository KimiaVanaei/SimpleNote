package ir.sharif.simplenote.feature.note.domain.usecase
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class ObserveNotesUseCase(private val repo: NotesRepository) {
    operator fun invoke(): Flow<List<Note>> = repo.observeNotes()
}
