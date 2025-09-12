// file: ir.sharif.simplenote/app/navigation/AppNavHost.kt
package ir.sharif.simplenote.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.sharif.simplenote.feature.home.ui.HomeScreen
import ir.sharif.simplenote.feature.settings.ui.ChangePasswordScreen
import ir.sharif.simplenote.feature.settings.ui.EditProfileScreen
import ir.sharif.simplenote.feature.settings.ui.SettingsScreen

object Routes {
    const val SETTINGS = "settings"
    const val EDIT_PROFILE = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"
    const val HOME = "home"
}

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenSettings = { navigateSingleTop(nav, Routes.SETTINGS) },
                onCreateNote = { /* nav.navigate("createNote") */ },
                onPressHome = { navigateSingleTop(nav, Routes.HOME) }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { nav.popBackStack() },
                onNavigateToEditProfile = { nav.navigate(Routes.EDIT_PROFILE) },
                onNavigateToChangePassword = { nav.navigate(Routes.CHANGE_PASSWORD) },
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onNavigateBack = { nav.popBackStack() },
                onSave = { _, _ -> nav.popBackStack() }
            )
        }

        composable(Routes.CHANGE_PASSWORD) {
            ChangePasswordScreen(
                onNavigateBack = { nav.popBackStack() },
                onSubmit = { _, _ -> nav.popBackStack() }
            )
        }
    }
}

private fun navigateSingleTop(nav: NavHostController, route: String) {
    nav.navigate(route) {
        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
