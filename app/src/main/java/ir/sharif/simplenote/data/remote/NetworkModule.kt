package ir.sharif.simplenote.data.remote

import android.content.Context
import ir.sharif.simplenote.data.remote.dto.TokenRefreshRequest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TokenStore(private val context: Context) {
    // Replace with DataStore; kept simple for brevity
    @Volatile private var access: String? = null
    @Volatile private var refresh: String? = null
    fun setTokens(access: String?, refresh: String?) { this.access = access; this.refresh = refresh }
    fun accessToken(): String? = access
    fun refreshToken(): String? = refresh
}

class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val token = tokenStore.accessToken()
        val newReq = if (token != null) req.newBuilder().addHeader("Authorization", "Bearer $token").build() else req
        return chain.proceed(newReq)
    }
}

class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val authService: AuthService
) : Authenticator {
    private val mutex = Mutex()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null
        val refresh = tokenStore.refreshToken() ?: return null
        var newAccess: String? = null
        runCatching {
            kotlinx.coroutines.runBlocking {
                mutex.withLock {
                    val current = tokenStore.accessToken()
                    if (current != null && response.request.header("Authorization") == "Bearer $current") {
                        val map = authService.refresh(TokenRefreshRequest(refresh))
                        newAccess = map["access"]
                        tokenStore.setTokens(newAccess, refresh)
                    }
                }
            }
        }
        val access = newAccess ?: return null
        return response.request.newBuilder()
            .header("Authorization", "Bearer $access")
            .build()
    }
    private fun responseCount(response: Response): Int {
        var result = 1
        var r = response.priorResponse
        while (r != null) { result++; r = r.priorResponse }
        return result
    }
}

object NetworkModuleFactory {
    fun create(
        baseUrl: String,
        context: Context,
        tokenStore: TokenStore
    ): Triple<Retrofit, NotesService, AuthService> {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
        val authRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client.newBuilder().authenticator(TokenAuthenticator(tokenStore, retrofit.create(AuthService::class.java))).build())
            .build()
        val notes = retrofit.create(NotesService::class.java)
        val auth = authRetrofit.create(AuthService::class.java)
        return Triple(retrofit, notes, auth)
    }
}