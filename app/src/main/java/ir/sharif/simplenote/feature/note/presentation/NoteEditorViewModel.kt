package ir.sharif.simplenote.feature.note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.core.util.UserProfileRepository
import kotlinx.coroutines.flow.first
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.usecase.AddNoteUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.DeleteNoteUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.GetNoteByIdUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class NoteEditorUiState(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val lastEdited: Long = System.currentTimeMillis(),
    val isNew: Boolean = true,
)

class NoteEditorViewModel(
    private val userProfileRepo: UserProfileRepository,
    private val getNote: GetNoteByIdUseCase,
    private val addNote: AddNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val syncNotes: SyncNotesUseCase,   // Added
    initialId: Int?
) : ViewModel() {

    private val _ui = MutableStateFlow(
        NoteEditorUiState(id = initialId, isNew = initialId == null)
    )
    val ui: StateFlow<NoteEditorUiState> = _ui

    // Track what’s already persisted
    private var persistedTitle: String = ""
    private var persistedContent: String = ""

    init {
        if (initialId != null) {
            viewModelScope.launch {
                val username = userProfileRepo.profile.first().username
                getNote(initialId, username)?.let { n ->
                    persistedTitle = n.title
                    persistedContent = n.content
                    _ui.update {
                        it.copy(
                            id = n.id,
                            title = n.title,
                            content = n.content,
                            lastEdited = n.lastEdited,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(v: String)   = _ui.update { it.copy(title = v) }
    fun onContentChange(v: String) = _ui.update { it.copy(content = v) }

    private fun hasChanges(): Boolean {
        val s = _ui.value
        return s.isNew || s.title != persistedTitle || s.content != persistedContent
    }

    fun save(onDone: (Int) -> Unit) = viewModelScope.launch {
        val s = _ui.value
        if (!hasChanges()) return@launch

        val username = userProfileRepo.profile.first().username
        val now = System.currentTimeMillis()
        if (s.isNew) {
            android.util.Log.d("NoteEditorVM", "Saving note for username=$username")
            val id = addNote(
                username,
                Note(
                    title = s.title,
                    content = s.content,
                    lastEdited = now,
                    isSynced = false,
                    username = username
                )
            )
            persistedTitle = s.title
            persistedContent = s.content
            _ui.update { it.copy(id = id, isNew = false, lastEdited = now) }
            syncNotes(username)
            onDone(id)
        } else {
            updateNote(
                username,
                Note(
                    id = s.id!!,
                    title = s.title,
                    content = s.content,
                    lastEdited = now,
                    isSynced = false,
                    username = username
                )
            )
            persistedTitle = s.title
            persistedContent = s.content
            _ui.update { it.copy(lastEdited = now) }
            syncNotes(username)
            onDone(s.id!!)
        }
    }

    fun onBack(onDone: () -> Unit) = viewModelScope.launch {
        val s = _ui.value
        val isEmpty = s.title.isBlank() && s.content.isBlank()
        if (isEmpty) {
            val username = userProfileRepo.profile.first().username
            s.id?.let { id ->
                deleteNote(
                    username,
                    Note(
                        id = id,
                        title = "",
                        content = "",
                        lastEdited = s.lastEdited,
                        username = username
                    )
                )
            }
            onDone()
        } else if (hasChanges()) {
            save { onDone() }
        } else {
            onDone()
        }
    }

    fun delete(onDone: () -> Unit) = viewModelScope.launch {
        val username = userProfileRepo.profile.first().username
        _ui.value.id?.let { id ->
            deleteNote(
                username,
                Note(
                    id = id,
                    title = "",
                    content = "",
                    lastEdited = _ui.value.lastEdited,
                    username = username
                )
            )
            syncNotes(username) // Sync afetr delete
        }
        onDone()
    }
}
