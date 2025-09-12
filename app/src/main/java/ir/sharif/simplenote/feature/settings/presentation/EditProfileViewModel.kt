package ir.sharif.simplenote.feature.settings.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.core.util.UserProfile
import ir.sharif.simplenote.core.util.UserProfileRepoProvider
import ir.sharif.simplenote.core.util.UserProfileRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    // 🔗 Same repo instance as SettingsViewModel
    private val repo: UserProfileRepository =
        UserProfileRepoProvider.get(application, viewModelScope)

    // Expose current profile so screen can prefill
    val profile: StateFlow<UserProfile> =
        repo.profile.stateIn(viewModelScope, SharingStarted.Eagerly, repo.profile.value)

    fun save(name: String, email: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repo.update(name.trim(), email.trim())
            onDone()
        }
    }

    fun updateAvatar(uri: String?) {
        viewModelScope.launch { repo.updateAvatar(uri) }
    }
}
