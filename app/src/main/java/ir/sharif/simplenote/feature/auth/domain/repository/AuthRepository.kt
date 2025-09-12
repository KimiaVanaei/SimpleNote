package ir.sharif.simplenote.feature.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedInFlow: Flow<Boolean>

    suspend fun login(username: String, password: String)
    suspend fun register(username: String, email: String, password: String)
    suspend fun logout()

    suspend fun getAccessOrNull(): String?
}

