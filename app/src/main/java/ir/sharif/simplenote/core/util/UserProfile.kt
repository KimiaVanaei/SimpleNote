package ir.sharif.simplenote.core.util

data class UserProfile(
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUri: String? = null
)
