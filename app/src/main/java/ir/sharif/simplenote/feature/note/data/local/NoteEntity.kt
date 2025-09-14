package ir.sharif.simplenote.feature.note.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val title: String,
    val content: String,
    val lastEdited: Long,
    val isSynced: Boolean = false
)
