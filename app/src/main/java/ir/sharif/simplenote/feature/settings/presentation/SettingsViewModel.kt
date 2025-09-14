package ir.sharif.simplenote.feature.settings.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ir.sharif.simplenote.feature.auth.domain.repository.AuthRepository
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.core.util.UserProfile
import ir.sharif.simplenote.core.util.UserProfileRepoProvider
import ir.sharif.simplenote.core.util.UserProfileRepository
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

    val profile: StateFlow<UserProfile> =
        repo.profile.stateIn(viewModelScope, SharingStarted.Eagerly, repo.profile.value)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onEvent(e: SettingsEvent) = when (e) {
        is SettingsEvent.ChangePassword -> {
            viewModelScope.launch {
                try {
                    val result = authRepo.changePassword(e.oldPassword, e.newPassword)
                    if (result.isSuccess) {
                        _uiState.update {
                            it.copy(
                                changePasswordSuccess = true,
                                changePasswordError = null,
                                message = "Password Changed Successfully"
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                changePasswordSuccess = false,
                                changePasswordError = "Failed To Change Password",
                                message = "Failed To Change Password"
                            )
                        }
                    }
                } catch (ex: Exception) {
                    _uiState.update {
                        it.copy(
                            changePasswordSuccess = false,
                            changePasswordError = ex.message,
                            message = "خطا: ${ex.message}"
                        )
                    }
                }
            }
        }

        SettingsEvent.DismissChangePasswordError -> {
            _uiState.update { it.copy(changePasswordError = null) }
        }

        SettingsEvent.OnOpenNotificationsDialog ->
            _uiState.update { it.copy(showNotificationsDialog = true) }

        SettingsEvent.OnDismissNotificationsDialog ->
            _uiState.update { it.copy(showNotificationsDialog = false) }

        is SettingsEvent.OnEmailNotificationsToggled ->
            _uiState.update { it.copy(emailNotificationsEnabled = e.enabled) }

        is SettingsEvent.OnPushNotificationsToggled ->
            _uiState.update { it.copy(pushNotificationsEnabled = e.enabled) }

        SettingsEvent.OnClickLogout ->
            _uiState.update { it.copy(showLogoutDialog = true) }

        SettingsEvent.OnDismissLogout ->
            _uiState.update { it.copy(showLogoutDialog = false) }

        SettingsEvent.ConfirmLogout -> {
            viewModelScope.launch {
                authRepo.logout()
                _uiState.update { it.copy(isLoggedOut = true, showLogoutDialog = false) }
            }
        }

        SettingsEvent.OnClickEditProfile,
        SettingsEvent.OnClickChangePassword -> Unit

        SettingsEvent.OnToggleDarkMode -> TODO()
    }
}
