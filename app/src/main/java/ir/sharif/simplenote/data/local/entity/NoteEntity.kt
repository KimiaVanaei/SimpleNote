package ir.sharif.simplenote.data.local.entity

import androidx.room.*


@Entity(
    tableName = "notes",
    indices = [
        Index(value = ["serverId"], unique = false),
        Index(value = ["updatedAt"], unique = false),
        Index(value = ["title"], unique = false)
    ]
)
data class NoteEntity(
    @PrimaryKey val localId: String, // UUID string
    val serverId: Int? = null,
    val title: String,
    val description: String,
    val createdAt: Long? = null, // epoch millis (UTC)
    val updatedAt: Long? = null, // epoch millis (UTC)
    val creatorName: String? = null,
    val creatorUsername: String? = null,
    val isDirty: Boolean = false,
    val isDeleted: Boolean = false,
    val lastModifiedLocal: Long = System.currentTimeMillis(),
    val syncError: String? = null,
)