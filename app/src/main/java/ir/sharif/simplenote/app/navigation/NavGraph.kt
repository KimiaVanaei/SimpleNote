package ir.sharif.simplenote.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.sharif.simplenote.feature.settings.ui.ChangePasswordScreen
import ir.sharif.simplenote.feature.settings.ui.EditProfileScreen
import ir.sharif.simplenote.feature.settings.ui.SettingsScreen

object Routes {
    const val SETTINGS = "settings"
    const val EDIT_PROFILE = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"
}

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Routes.SETTINGS) {
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { /* root */ },
                onNavigateToEditProfile = { nav.navigate(Routes.EDIT_PROFILE) },
                onNavigateToChangePassword = { nav.navigate(Routes.CHANGE_PASSWORD) },
            )
        }
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onNavigateBack = { nav.popBackStack() },
                onSave = { _, _ -> nav.popBackStack() } // later: send to VM/repo
            )
        }
        composable(Routes.CHANGE_PASSWORD) {
            ChangePasswordScreen(
                onNavigateBack = { nav.popBackStack() },
                onSubmit = { _, _ -> nav.popBackStack() } // later: send to VM/repo
            )
        }
    }
}

