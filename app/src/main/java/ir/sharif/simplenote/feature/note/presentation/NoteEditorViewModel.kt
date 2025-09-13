package ir.sharif.simplenote.feature.note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getNote: GetNoteByIdUseCase,
    private val addNote: AddNoteUseCase,      // returns Int
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
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
                getNote(initialId)?.let { n ->
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

    /** Save ONLY if there are changes. Calls onDone with the note id when a save actually happened. */
    fun save(onDone: (Int) -> Unit) = viewModelScope.launch {
        val s = _ui.value
        if (!hasChanges()) return@launch    // ⬅️ nothing to do

        val now = System.currentTimeMillis()
        if (s.isNew) {
            val id = addNote(
                Note(title = s.title, content = s.content, lastEdited = now, isSynced = false)
            )
            persistedTitle = s.title
            persistedContent = s.content
            _ui.update { it.copy(id = id, isNew = false, lastEdited = now) }
            onDone(id)
        } else {
            updateNote(
                Note(id = s.id!!, title = s.title, content = s.content, lastEdited = now, isSynced = false)
            )
            persistedTitle = s.title
            persistedContent = s.content
            _ui.update { it.copy(lastEdited = now) }
            onDone(s.id!!)
        }
    }

    /** Back/up: delete if empty; else save only if changed; else just exit. */
    fun onBack(onDone: () -> Unit) = viewModelScope.launch {
        val s = _ui.value
        val isEmpty = s.title.isBlank() && s.content.isBlank()
        if (isEmpty) {
            s.id?.let { deleteNote(Note(id = it, title = "", content = "", lastEdited = s.lastEdited)) }
            onDone()
        } else if (hasChanges()) {
            save { onDone() }   // will update lastEdited
        } else {
            onDone()            // ⬅️ no change → leave lastEdited untouched
        }
    }

    fun delete(onDone: () -> Unit) = viewModelScope.launch {
        _ui.value.id?.let { id ->
            deleteNote(Note(id = id, title = "", content = "", lastEdited = _ui.value.lastEdited))
        }
        onDone()
    }
}