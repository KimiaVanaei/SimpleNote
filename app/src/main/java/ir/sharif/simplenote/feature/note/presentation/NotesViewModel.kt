package ir.sharif.simplenote.feature.note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.feature.note.domain.usecase.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NotesViewModel(
    private val getNotes: GetNotesUseCase,
    private val addNote: AddNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val searchNotes: SearchNotesUseCase
) : ViewModel() {

    var uiState by mutableStateOf(NotesUiState())
        private set

    fun loadNotes() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val list = getNotes()
                uiState = uiState.copy(notes = list, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun add(title: String, content: String, color: String) {
        viewModelScope.launch {
            addNote(
                Note(
                    title = title,
                    content = content,
                    color = color,
                    lastEdited = System.currentTimeMillis()
                )
            )
            loadNotes()
        }
    }

    fun update(note: Note) {
        viewModelScope.launch {
            updateNote(note.copy(lastEdited = System.currentTimeMillis()))
            loadNotes()
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            deleteNote(note)
            loadNotes()
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val result = searchNotes(query)
                uiState = uiState.copy(notes = result)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message)
            }
        }
    }
}

