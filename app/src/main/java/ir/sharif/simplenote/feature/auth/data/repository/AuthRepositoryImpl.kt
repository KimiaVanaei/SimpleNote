package ir.sharif.simplenote.feature.auth.data.repository

import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore
import ir.sharif.simplenote.feature.auth.data.local.Tokens
import ir.sharif.simplenote.feature.auth.data.remote.AuthApi
import ir.sharif.simplenote.feature.auth.data.remote.ChangePasswordRequest
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

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            api.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password,
                    first_name = firstName,
                    last_name = lastName
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            ds.clear()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAccessOrNull(): String? {
        return ds.tokensFlow.firstOrNull()?.access
    }

    suspend fun refreshToken() {
        val refresh = ds.tokensFlow.firstOrNull()?.refresh ?: return
        val newAccess = api.refresh(RefreshRequest(refresh))
        ds.updateAccess(newAccess.access)
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return try {
            val token = getAccessOrNull() ?: return Result.failure(Exception("No token"))
            android.util.Log.d("AuthRepo", ">>> Sending change password request")
            android.util.Log.d("AuthRepo", "Payload = { old=$oldPassword , new=$newPassword }")
            android.util.Log.d("AuthRepo", "Token = Bearer $token")

            val response = api.changePassword(
                body = mapOf(
                    "old_password" to oldPassword,
                    "new_password" to newPassword
                ),
                token = "Bearer $token"
            )

            android.util.Log.d("AuthRepo", "<<< Response code = ${response.code()}")


            if (response.isSuccessful) {
                android.util.Log.d("AuthRepo", "Password changed successfully!")

                Result.success(Unit)
            } else {
                android.util.Log.e("AuthRepo", "<<< Failed: ${response.code()} ${response.errorBody()?.string()}")
                Result.failure(Exception("Change password failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepo", "Exception: ${e.message}")
            Result.failure(e)
        }
    }
}
