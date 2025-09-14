package ir.sharif.simplenote.feature.auth.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Tokens(val access: String, val refresh: String)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthDataStore private constructor(private val ds: DataStore<Preferences>) {

    private val K_ACCESS = stringPreferencesKey("auth_access")
    private val K_REFRESH = stringPreferencesKey("auth_refresh")

    val tokensFlow: Flow<Tokens?> = ds.data.map { p ->
        val a = p[K_ACCESS]
        val r = p[K_REFRESH]
        if (a != null && r != null) Tokens(a, r) else null
    }

    suspend fun save(tokens: Tokens) = ds.edit {
        it[K_ACCESS] = tokens.access
        it[K_REFRESH] = tokens.refresh
    }

    suspend fun updateAccess(newAccess: String) = ds.edit {
        it[K_ACCESS] = newAccess
    }

    suspend fun clear() = ds.edit {
        it.remove(K_ACCESS)
        it.remove(K_REFRESH)
    }

    companion object {
        @Volatile private var INSTANCE: AuthDataStore? = null

        fun getInstance(context: Context): AuthDataStore {
            return INSTANCE ?: synchronized(this) {
                val ds = AuthDataStore(context.dataStore)
                INSTANCE = ds
                ds
            }
        }
    }
}
