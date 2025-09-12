package ir.sharif.simplenote.feature.auth.presentation

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val retypePassword: String = "",
    val showPassword: Boolean = false,
    val showRetype: Boolean = false
)
