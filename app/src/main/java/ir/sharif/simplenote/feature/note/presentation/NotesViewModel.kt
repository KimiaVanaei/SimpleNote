package ir.sharif.simplenote.feature.note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class NotesViewModel(
    observeNotes: ObserveNotesUseCase,              // Flow<List<Note>>
    private val addNote: AddNoteUseCase,           // returns Int
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val notesFlow: Flow<List<Note>> = observeNotes()

    val ui: StateFlow<NotesUiState> =
        combine(notesFlow, query) { all, q ->
            val filtered = if (q.isBlank()) all else {
                all.filter { n ->
                    n.title.contains(q, ignoreCase = true) ||
                            n.content.contains(q, ignoreCase = true)
                }
            }
            NotesUiState(
                notes = filtered,
                query = q,
                hasAny = all.isNotEmpty()
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            NotesUiState()
        )

    fun onQueryChange(q: String) { query.value = q }

    /** Option B: create in DB and return new id */
    fun addBlankNote(onCreated: (Int) -> Unit) {
        val now = System.currentTimeMillis()
        viewModelScope.launch {
            val id = addNote(Note(title = "", content = "", lastEdited = now, isSynced = false))
            onCreated(id)
        }
    }

    // optional helpers if you edit from cards, etc.
    fun update(note: Note) = viewModelScope.launch { updateNote(note) }
    fun delete(note: Note) = viewModelScope.launch { deleteNote(note) }

    /** Optional one-time cleanup of orphan blanks (call in Home once) */
    fun pruneEmptyNotes() = viewModelScope.launch {}
}
