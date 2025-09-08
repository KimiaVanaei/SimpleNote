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
    val createdAt: Date,
    val updatedAt: Date,
    val creatorName: String,
    val creatorUsername: String
)