package ir.sharif.simplenote.feature.note.domain.model

data class Note(
    //val id: Int = 0,
    val localId: Int = 0,              // Room PK
    val serverId: Int? = null,         // Django ID
    val title: String,
    val content: String,
    val lastEdited: Long,
    val isSynced: Boolean = false,
    val username: String
)
