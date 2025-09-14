package ir.sharif.simplenote.feature.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Tokens(val access: String, val refresh: String)

class AuthDataStore(private val ds: DataStore<Preferences>) {

    private val K_ACCESS = stringPreferencesKey("auth_access")
    private val K_REFRESH = stringPreferencesKey("auth_refresh")

    val tokensFlow: Flow<Tokens?> = ds.data.map { p ->
        val a = p[K_ACCESS]
        val r = p[K_REFRESH]
        if (a != null && r != null) Tokens(a, r) else null
    }

    suspend fun save(tokens: Tokens) = ds.edit {
        android.util.Log.d("AuthDS", "Saving tokens: access=${tokens.access.take(20)}..., refresh=${tokens.refresh.take(20)}...")
        it[K_ACCESS] = tokens.access
        it[K_REFRESH] = tokens.refresh
    }

    suspend fun updateAccess(newAccess: String) = ds.edit {
        android.util.Log.d("AuthDS", "Updating access token: ${newAccess.take(20)}...")
        it[K_ACCESS] = newAccess
    }

    suspend fun clear() = ds.edit {
        android.util.Log.d("AuthDS", "Clearing all tokens from DataStore")
        it.remove(K_ACCESS)
        it.remove(K_REFRESH)
        // it.clear()
    }
}

