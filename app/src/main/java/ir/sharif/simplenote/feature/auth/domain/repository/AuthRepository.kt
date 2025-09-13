package ir.sharif.simplenote.feature.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedInFlow: Flow<Boolean>

    suspend fun login(username: String, password: String)

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Result<Unit>

    suspend fun logout()

    suspend fun getAccessOrNull(): String?
}

