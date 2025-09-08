package ir.sharif.simplenote.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val accessToken: String?,
    val refreshToken: String?
)