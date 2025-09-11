package ir.sharif.simplenote.data.user

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// DataStore instance (scoped to Context)
private val Context.userProfileDataStore by preferencesDataStore(name = "user_profile")

interface UserProfileRepository {
    val profile: StateFlow<UserProfile>
    suspend fun update(name: String, email: String)
    suspend fun updateAvatar(uri: String?)
}

/**
 * Hybrid: keeps a StateFlow in memory for instant UI updates,
 * and persists to DataStore so values survive app restarts.
 */
class HybridUserProfileRepository(
    private val context: Context,
    private val scope: CoroutineScope
) : UserProfileRepository {

    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_AVATAR = stringPreferencesKey("avatar")

    private val _profile = MutableStateFlow(UserProfile())
    override val profile: StateFlow<UserProfile> = _profile

    init {
        // Keep memory cache synced with DataStore
        scope.launch {
            context.userProfileDataStore.data
                .map { prefs ->
                    UserProfile(
                        name = prefs[KEY_NAME] ?: "",
                        email = prefs[KEY_EMAIL] ?: "",
                        avatarUri = prefs[KEY_AVATAR]
                    )
                }
                .collect { loaded -> _profile.value = loaded }
        }
    }

    override suspend fun update(name: String, email: String) {
        _profile.value = _profile.value.copy(name = name, email = email)
        context.userProfileDataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
        }
    }

    override suspend fun updateAvatar(uri: String?) {
        _profile.value = _profile.value.copy(avatarUri = uri)
        context.userProfileDataStore.edit { prefs ->
            if (uri == null) prefs.remove(KEY_AVATAR) else prefs[KEY_AVATAR] = uri
        }
    }
}

/**
 * Tiny provider to give both ViewModels the *same* repo instance.
 * No DI required.
 */
object UserProfileRepoProvider {
    @SuppressLint("StaticFieldLeak")
    @Volatile private var instance: HybridUserProfileRepository? = null

    fun get(context: Context, scope: CoroutineScope): UserProfileRepository {
        return instance ?: synchronized(this) {
            instance ?: HybridUserProfileRepository(context.applicationContext, scope).also {
                instance = it
            }
        }
    }
}