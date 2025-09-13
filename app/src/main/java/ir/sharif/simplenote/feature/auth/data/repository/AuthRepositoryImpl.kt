package ir.sharif.simplenote.feature.auth.data.repository

import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore
import ir.sharif.simplenote.feature.auth.data.local.Tokens
import ir.sharif.simplenote.feature.auth.data.remote.AuthApi
import ir.sharif.simplenote.feature.auth.data.remote.LoginRequest
import ir.sharif.simplenote.feature.auth.data.remote.RegisterRequest
import ir.sharif.simplenote.feature.auth.data.remote.RefreshRequest
import ir.sharif.simplenote.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class AuthRepositoryImpl(
    private val api: AuthApi,
    private val ds: AuthDataStore
) : AuthRepository {

    override val isLoggedInFlow: Flow<Boolean> =
        ds.tokensFlow.map { it != null }

    override suspend fun login(username: String, password: String) {
        android.util.Log.d("AuthRepo", ">>> Sending login request")
        android.util.Log.d("AuthRepo", "Payload = { username=$username , passwordLength=${password.length} }")

        val tp = api.login(LoginRequest(username, password))

        android.util.Log.d("AuthRepo", "<<< Response received")
        android.util.Log.d("AuthRepo", "Access token (first 20 chars): ${tp.access.take(20)}...")
        android.util.Log.d("AuthRepo", "Refresh token (first 20 chars): ${tp.refresh.take(20)}...")

        ds.save(Tokens(tp.access, tp.refresh))
        android.util.Log.d("AuthRepo", "Tokens saved into DataStore")
    }

    override suspend fun register(username: String, email: String, password: String) {
        api.register(RegisterRequest(username, email, password))
        login(username, password)
    }

    override suspend fun logout() {
        ds.clear()
    }

    override suspend fun getAccessOrNull(): String? {
        return ds.tokensFlow.firstOrNull()?.access
    }

    suspend fun refreshToken() {
        val refresh = ds.tokensFlow.firstOrNull()?.refresh ?: return
        val newAccess = api.refresh(RefreshRequest(refresh))
        ds.updateAccess(newAccess.access)
    }
}
