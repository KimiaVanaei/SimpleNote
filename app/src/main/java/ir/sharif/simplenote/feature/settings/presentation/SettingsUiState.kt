package ir.sharif.simplenote.feature.settings.presentation

data class SettingsUiState(
    val notificationsEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showNotificationsDialog: Boolean = false,

    // NEW: split notification types
    val emailNotificationsEnabled: Boolean = false,
    val pushNotificationsEnabled: Boolean = false,
)
