package ir.sharif.simplenote.feature.note.domain.model

data class Note(
    val id: Int = 0,
    val username: String,
    val title: String,
    val content: String,
    val lastEdited: Long,               // Timestamp
    val isSynced: Boolean = false       // for checking with Server
)