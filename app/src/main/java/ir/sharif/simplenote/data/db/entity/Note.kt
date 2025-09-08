package ir.sharif.simplenote.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val created_at: Date,
    val updated_at: Date,
    val creator_name: String,
    val creator_username: String
)