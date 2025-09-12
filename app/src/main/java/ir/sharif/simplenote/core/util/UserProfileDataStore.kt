package ir.sharif.simplenote.core.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

private val Context.userProfileDataStore by preferencesDataStore(name = "user_profile")

class DataStoreUserProfileRepository(private val context: Context) : UserProfileRepository {

    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_AVATAR = stringPreferencesKey("avatar")

    override val profile: StateFlow<UserProfile> = MutableStateFlow(UserProfile())

    override suspend fun update(name: String, email: String) {
        context.userProfileDataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
        }
    }

    override suspend fun updateAvatar(uri: String?) {
        context.userProfileDataStore.edit { prefs ->
            if (uri == null) prefs.remove(KEY_AVATAR)
            else prefs[KEY_AVATAR] = uri
        }
    }
}
