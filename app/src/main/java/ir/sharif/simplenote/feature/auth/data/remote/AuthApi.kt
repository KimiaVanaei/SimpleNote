package ir.sharif.simplenote.feature.auth.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

// Requests
data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val email: String, val password: String)
data class RefreshRequest(val refresh: String)

// Responses
data class TokenPairDto(val access: String, val refresh: String)
data class AccessDto(val access: String)
data class UserDto(val id: Long, val username: String, val email: String?)

// Retrofit Interface
interface AuthApi {
    @POST("api/auth/login/")
    suspend fun login(@Body body: LoginRequest): TokenPairDto

    @POST("api/auth/register/")
    suspend fun register(@Body body: RegisterRequest): UserDto

    @POST("api/auth/refresh/")
    suspend fun refresh(@Body body: RefreshRequest): AccessDto

    @POST("api/auth/logout/")
    suspend fun logout()
}
