package ir.sharif.simplenote.feature.auth.presentation

import android.util.Log
import retrofit2.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.sharif.simplenote.core.util.UserProfileRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val profileRepo: UserProfileRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(v: String) { uiState = uiState.copy(email = v) }
    fun onPasswordChange(v: String) { uiState = uiState.copy(password = v) }
    fun togglePasswordVisibility() { uiState = uiState.copy(showPassword = !uiState.showPassword) }

    fun login() {
        viewModelScope.launch {
            android.util.Log.d("LoginVM", ">>> login() called with username=${uiState.email}, passwordLength=${uiState.password.length}")
            uiState = uiState.copy(isLoading = true, error = null)
            runCatching {
                repo.login(uiState.email.trim(), uiState.password)
            }.onSuccess { user ->
                android.util.Log.d("LoginVM", "<<< Login success, updating UI state")
                profileRepo.update(
                    name = "",
                    email = uiState.email,
                    username = uiState.email
                )
                uiState = uiState.copy(isLoading = false, success = true)
            }.onFailure { e ->
                if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    android.util.Log.e("LoginVM", "Login failed with HTTP ${e.code()}: $errorBody")
                    uiState = uiState.copy(isLoading = false, error = errorBody)
                } else {
                    android.util.Log.e("LoginVM", "Login failed with exception", e)
                    uiState = uiState.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
}

