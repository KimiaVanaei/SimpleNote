package ir.sharif.simplenote.core.util

import android.content.Context
import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object AuthDataStoreProvider {
    @Volatile private var instance: AuthDataStore? = null

    fun get(context: Context): AuthDataStore {
        return instance ?: synchronized(this) {
            instance ?: AuthDataStore.getInstance(context).also { instance = it }
        }
    }

    fun getAccessToken(context: Context): String? {
        val ds = get(context)
        return runBlocking {
            ds.tokensFlow.firstOrNull()?.access
        }
    }
}
