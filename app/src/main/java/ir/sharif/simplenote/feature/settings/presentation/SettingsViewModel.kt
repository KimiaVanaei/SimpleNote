package ir.sharif.simplenote.feature.settings.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ir.sharif.simplenote.feature.auth.domain.repository.AuthRepository
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.user.UserProfile
import ir.sharif.simplenote.data.user.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: UserProfileRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    // Expose live profile to UI
    val profile: StateFlow<UserProfile> =
        repo.profile.stateIn(viewModelScope, SharingStarted.Eagerly, repo.profile.value)

    // --- your existing UI state (kept intact) ---
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onEvent(e: SettingsEvent) = when (e) {
        // notifications dialog
        SettingsEvent.OnOpenNotificationsDialog ->
            _uiState.update { it.copy(showNotificationsDialog = true) }

        SettingsEvent.ConfirmLogout -> {
            viewModelScope.launch {
                authRepo.logout()
                _uiState.update { it.copy(showLogoutDialog = false, isLoggedOut = true) }
            }
        }

        SettingsEvent.OnDismissNotificationsDialog ->
            _uiState.update { it.copy(showNotificationsDialog = false) }

        is SettingsEvent.OnEmailNotificationsToggled ->
            _uiState.update { it.copy(emailNotificationsEnabled = e.enabled) }

        is SettingsEvent.OnPushNotificationsToggled ->
            _uiState.update { it.copy(pushNotificationsEnabled = e.enabled) }

        // logout
        SettingsEvent.OnClickLogout ->
            _uiState.update { it.copy(showLogoutDialog = true) }

        SettingsEvent.OnDismissLogout ->
            _uiState.update { it.copy(showLogoutDialog = false) }

        // nav taps – handled by the screen via callbacks
        SettingsEvent.OnClickEditProfile,
        SettingsEvent.OnClickChangePassword -> Unit

        SettingsEvent.OnToggleDarkMode -> TODO()
    }
}
