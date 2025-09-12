package ir.sharif.simplenote.feature.note.domain.model

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val color: String,
    val lastEdited: Long,               // Timestamp
    val isSynced: Boolean = false       // for checking with Server
)

