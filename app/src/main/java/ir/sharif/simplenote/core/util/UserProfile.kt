package ir.sharif.simplenote.core.util

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val avatarUri: String? = null // content:// or file://
)
