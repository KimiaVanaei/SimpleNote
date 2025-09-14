package ir.sharif.simplenote.feature.settings.presentation

sealed interface SettingsEvent {
    // prefs
    data object OnToggleDarkMode : SettingsEvent

    // notifications dialog + toggles
    data object OnOpenNotificationsDialog : SettingsEvent
    data object OnDismissNotificationsDialog : SettingsEvent
    data class OnEmailNotificationsToggled(val enabled: Boolean) : SettingsEvent
    data class OnPushNotificationsToggled(val enabled: Boolean) : SettingsEvent

    // logout dialog
    data object OnClickLogout : SettingsEvent
    data object OnDismissLogout : SettingsEvent

    // navigation taps
    data object OnClickEditProfile : SettingsEvent
    data object OnClickChangePassword : SettingsEvent
    data object ConfirmLogout : SettingsEvent
    data class ChangePassword(val oldPassword: String, val newPassword: String) : SettingsEvent
    data object DismissChangePasswordError : SettingsEvent
}