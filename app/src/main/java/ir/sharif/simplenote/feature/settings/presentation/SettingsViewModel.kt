package ir.sharif.simplenote.feature.settings.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onEvent(e: SettingsEvent) = when (e) {
        // dark mode
        SettingsEvent.OnToggleDarkMode ->
            _uiState.update { it.copy(darkModeEnabled = !it.darkModeEnabled) }

        // notifications dialog
        SettingsEvent.OnOpenNotificationsDialog ->
            _uiState.update { it.copy(showNotificationsDialog = true) }

        SettingsEvent.OnDismissNotificationsDialog ->
            _uiState.update { it.copy(showNotificationsDialog = false) }

        is SettingsEvent.OnEmailNotificationsToggled ->
            _uiState.update { it.copy(emailNotificationsEnabled = e.enabled) }

        is SettingsEvent.OnPushNotificationsToggled ->
            _uiState.update { it.copy(pushNotificationsEnabled = e.enabled) }

        // logout dialog
        SettingsEvent.OnClickLogout ->
            _uiState.update { it.copy(showLogoutDialog = true) }

        SettingsEvent.OnDismissLogout ->
            _uiState.update { it.copy(showLogoutDialog = false) }

        // nav taps (handled by screen callbacks)
        SettingsEvent.OnClickEditProfile,
        SettingsEvent.OnClickChangePassword -> Unit
    }
}
