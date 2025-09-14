package ir.sharif.simplenote.core.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.userProfileDataStore by preferencesDataStore(name = "user_profile")

class DataStoreUserProfileRepository(private val context: Context) : UserProfileRepository {

    private val KEY_USERNAME = stringPreferencesKey("username")
    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_AVATAR = stringPreferencesKey("avatar")

    private val _profile = MutableStateFlow(UserProfile())
    override val profile: StateFlow<UserProfile> = _profile

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.userProfileDataStore.data
                .map { prefs ->
                    UserProfile(
                        username = prefs[KEY_USERNAME] ?: "",
                        name = prefs[KEY_NAME] ?: "",
                        email = prefs[KEY_EMAIL] ?: "",
                        avatarUri = prefs[KEY_AVATAR]
                    )
                }
                .collect { loaded ->
                    _profile.value = loaded
                }
        }
    }

    override suspend fun update(name: String, email: String, username: String) {
        context.userProfileDataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
            prefs[KEY_USERNAME] = username
        }
    }


    override suspend fun updateAvatar(uri: String?) {
        context.userProfileDataStore.edit { prefs ->
            if (uri == null) prefs.remove(KEY_AVATAR)
            else prefs[KEY_AVATAR] = uri
        }
    }
}
