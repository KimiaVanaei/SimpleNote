package ir.sharif.simplenote.feature.auth.data.repository

import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore
import ir.sharif.simplenote.feature.auth.data.local.Tokens
import ir.sharif.simplenote.feature.auth.data.remote.AuthApi
import ir.sharif.simplenote.feature.auth.data.remote.LoginRequest
import ir.sharif.simplenote.feature.auth.data.remote.RegisterRequest
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
        val tp = api.login(LoginRequest(username, password))
        ds.save(Tokens(tp.access, tp.refresh))
    }

    override suspend fun register(username: String, email: String, password: String) {
        api.register(RegisterRequest(username, email, password))
        login(username, password)
    }

    override suspend fun logout() {
        runCatching { api.logout() }
        ds.clear()
    }

    override suspend fun getAccessOrNull(): String? {
        return ds.tokensFlow.firstOrNull()?.access
    }
}
