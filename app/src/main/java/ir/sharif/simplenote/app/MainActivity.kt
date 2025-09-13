package ir.sharif.simplenote.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import ir.sharif.simplenote.app.navigation.AppNavHost
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore

private val ComponentActivity.dataStore by preferencesDataStore(name = "auth_prefs")

class MainActivity : ComponentActivity() {

    private lateinit var authDataStore: AuthDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authDataStore = AuthDataStore(dataStore)

        setContent {
            SimpleNoteTheme {
                val nav = rememberNavController()
                MaterialTheme {
                    AppNavHost(
                        nav = nav,
                        isOnboarded = true,
                        isLoggedInFlow = authDataStore.tokensFlow.map { it != null }
                    )
                }
            }
        }
    }
}
