package ir.sharif.simplenote.data.user

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val avatarUri: String? = null // content:// or file://
)