package ir.sharif.simplenote.feature.auth.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Header

// Requests
data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String
)
data class RefreshRequest(val refresh: String)

data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String
)

// Responses
data class TokenPairDto(val access: String, val refresh: String)
data class AccessDto(val access: String)
data class UserDto(val id: Long, val username: String, val email: String?)

// Retrofit Interface
interface AuthApi {
    @POST("api/auth/token/")
    suspend fun login(@Body body: LoginRequest): TokenPairDto

    @POST("api/auth/register/")
    suspend fun register(@Body body: RegisterRequest): UserDto

    @POST("api/auth/token/refresh/")
    suspend fun refresh(@Body body: RefreshRequest): AccessDto

    @POST("api/auth/change-password/")
    suspend fun changePassword(
        @Body body: Map<String, String>,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>
}