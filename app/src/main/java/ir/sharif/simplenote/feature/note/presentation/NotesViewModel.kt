package ir.sharif.simplenote.feature.note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.core.util.UserProfileRepository
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(
    private val userProfileRepo: UserProfileRepository,
    private val observeNotes: ObserveNotesUseCase,
    private val addNote: AddNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val usernameFlow: Flow<String> =
        userProfileRepo.profile
            .onEach { android.util.Log.d("NotesVM", "Observing notes for username=${it.username}") }
            .map { it.username }

    private val notesFlow: Flow<List<Note>> =
        usernameFlow.flatMapLatest { username ->
            observeNotes(username)
        }

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
            usernameFlow.firstOrNull()?.let { username ->
                val id = addNote(username, Note(
                    title = "",
                    content = "",
                    lastEdited = now,
                    isSynced = false,
                    username = username
                ))
                onCreated(id)
            }
        }
    }

    fun update(note: Note) = viewModelScope.launch {
        usernameFlow.firstOrNull()?.let { username ->
            updateNote(username, note)
        }
    }

    fun delete(note: Note) = viewModelScope.launch {
        usernameFlow.firstOrNull()?.let { username ->
            deleteNote(username, note)
        }
    }

    /** Optional one-time cleanup of orphan blanks (call in Home once) */
    fun pruneEmptyNotes() = viewModelScope.launch {}
}
