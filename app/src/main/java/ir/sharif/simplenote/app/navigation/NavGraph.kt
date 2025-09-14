package ir.sharif.simplenote.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.flow.Flow

/** Route containers (graphs) */
internal object Graph {
    const val AUTH = "graph_auth"
    const val APP  = "graph_app"
}

/** Leaf routes within graphs */
object Routes {
    // Auth
    const val ONBOARDING = "onboarding"
    const val LOGIN      = "login"
    const val REGISTER   = "register"

    // App
    const val HOME            = "home"
    const val SETTINGS        = "settings"
    const val EDIT_PROFILE    = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"
    const val EDIT_NOTE       = "edit_note"
}

@Composable
fun AppNavHost(
    nav: NavHostController,
    isOnboarded: Boolean = false,
    isLoggedInFlow: Flow<Boolean>
) {
    val isLoggedInState = isLoggedInFlow.collectAsState(initial = null)

    if (isLoggedInState.value == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val isLoggedIn = isLoggedInState.value ?: false

    when {
        isLoggedIn -> {
            NavHost(
                navController = nav,
                startDestination = Graph.APP
            ) {
                addAppGraph(nav)
            }
        }
        else -> {
            NavHost(
                navController = nav,
                startDestination = Graph.AUTH
            ) {
                addOnboardingGraph(nav)
            }
        }
    }
}
