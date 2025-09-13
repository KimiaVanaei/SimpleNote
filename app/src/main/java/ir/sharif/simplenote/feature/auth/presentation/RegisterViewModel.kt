package ir.sharif.simplenote.feature.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import ir.sharif.simplenote.feature.auth.domain.repository.AuthRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState
    fun onFirstNameChange(v: String) {
        _uiState.update { it.copy(firstName = v) }
    }
    fun onLastNameChange(v: String) {
        _uiState.update { it.copy(lastName = v) }
    }
    fun onUsernameChange(v: String) {
        _uiState.update { it.copy(username = v) }
    }
    fun onEmailChange(v: String) {
        _uiState.update { it.copy(email = v) }
    }
    fun onPasswordChange(v: String) {
        _uiState.update { it.copy(password = v) }
    }
    fun onRetypeChange(v: String) {
        _uiState.update { it.copy(retypePassword = v) }
    }
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }
    fun toggleRetypeVisibility() {
        _uiState.update { it.copy(showRetype = !it.showRetype) }
    }

    fun register() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val s = _uiState.value
                Log.d(
                    "RegisterVM",
                    "Payload = { username=${s.username}, email=${s.email}, passwordLength=${s.password.length}, firstName=${s.firstName}, lastName=${s.lastName} }"
                )

                val result = repo.register(
                    firstName = _uiState.value.firstName,
                    lastName  = _uiState.value.lastName,
                    username  = _uiState.value.username,
                    email     = _uiState.value.email,
                    password  = _uiState.value.password
                )

                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false, success = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Registration failed") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
