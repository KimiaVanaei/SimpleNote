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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(v: String) { uiState = uiState.copy(email = v) }
    fun onPasswordChange(v: String) { uiState = uiState.copy(password = v) }
    fun togglePasswordVisibility() { uiState = uiState.copy(showPassword = !uiState.showPassword) }

    fun login() {
        viewModelScope.launch {
            Log.d("LoginVM", "login() called with ${uiState.email}")
            uiState = uiState.copy(isLoading = true, error = null)
            runCatching {
                repo.login(uiState.email.trim(), uiState.password)
            }.onSuccess {
                Log.d("LoginVM", "Login success")
                uiState = uiState.copy(isLoading = false, success = true)
            }.onFailure { e ->
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("LoginVM", "Login failed with ${e.code()}: $errorBody")
                    uiState = uiState.copy(isLoading = false, error = errorBody)
                } else {
                    Log.e("LoginVM", "Login failed", e)
                    uiState = uiState.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
}

