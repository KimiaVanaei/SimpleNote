package ir.sharif.simplenote.domain.model

data class Note(
    val localId: String,
    val serverId: Int?,
    val title: String,
    val description: String,
    val createdAt: Long?, // epoch millis (UTC)
    val updatedAt: Long?, // epoch millis (UTC)
    val creatorName: String?,
    val creatorUsername: String?,
    val isDirty: Boolean,
    val isDeleted: Boolean,
)