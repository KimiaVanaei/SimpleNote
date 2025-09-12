package ir.sharif.simplenote.feature.note.presentation

import ir.sharif.simplenote.feature.note.domain.model.Note

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

