package ir.sharif.simplenote.feature.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onFirstNameChange(v: String) { uiState = uiState.copy(firstName = v) }
    fun onLastNameChange(v: String) { uiState = uiState.copy(lastName = v) }
    fun onUsernameChange(v: String) { uiState = uiState.copy(username = v) }
    fun onEmailChange(v: String) { uiState = uiState.copy(email = v) }
    fun onPasswordChange(v: String) { uiState = uiState.copy(password = v) }
    fun onRetypeChange(v: String) { uiState = uiState.copy(retypePassword = v) }
    fun togglePasswordVisibility() { uiState = uiState.copy(showPassword = !uiState.showPassword) }
    fun toggleRetypeVisibility() { uiState = uiState.copy(showRetype = !uiState.showRetype) }
}
