package ir.sharif.simplenote.feature.auth.presentation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false
)
