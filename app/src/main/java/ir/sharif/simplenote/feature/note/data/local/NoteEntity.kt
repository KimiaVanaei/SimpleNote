package ir.sharif.simplenote.feature.note.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    indices = [Index(value = ["serverId", "username"], unique = true)]
)
data class NoteEntity(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val serverId: Int? = null,
    val title: String,
    val content: String,
    val lastEdited: Long,
    val isSynced: Boolean,
    val username: String
)

enum class PendingAction { NONE, CREATE, UPDATE, DELETE }
