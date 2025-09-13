package ir.sharif.simplenote.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ir.sharif.simplenote.feature.auth.ui.LoginScreen
import ir.sharif.simplenote.feature.auth.ui.OnboardingScreen
import ir.sharif.simplenote.feature.auth.ui.RegisterScreen
import ir.sharif.simplenote.feature.home.ui.HomeScreen
import ir.sharif.simplenote.feature.settings.ui.ChangePasswordScreen
import ir.sharif.simplenote.feature.settings.ui.EditProfileScreen
import ir.sharif.simplenote.feature.settings.ui.SettingsScreen

/** Route containers (graphs) */
private object Graph {
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
    const val HOME           = "home"
    const val SETTINGS       = "settings"
    const val EDIT_PROFILE   = "edit_profile"
    const val CHANGE_PASSWORD= "change_password"
}

/**
 * Pass flags if you want to skip onboarding or auth based on persisted state later.
 * For now defaults show Onboarding first.
 */
@Composable
fun AppNavHost(
    nav: NavHostController,
    isOnboarded: Boolean = false,   // e.g., from DataStore
    isLoggedInFlow: Flow<Boolean>   // e.g., from auth state
) {

    val isLoggedIn by isLoggedInFlow.collectAsState(initial = false)

    val startGraph = when {
        !isOnboarded -> Graph.AUTH
        !isLoggedIn  -> Graph.AUTH
        else         -> Graph.APP
    }

    NavHost(navController = nav, startDestination = startGraph) {

        /* ---------------------- AUTH GRAPH ---------------------- */
        navigation(startDestination = Routes.ONBOARDING, route = Graph.AUTH) {

            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onGetStartedClick = { nav.navigate(Routes.LOGIN) }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginClick = { _, _ ->
                        // Login success → enter APP graph, clear AUTH graph
                        nav.navigate(Graph.APP) {
                            popUpTo(Graph.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onRegisterClick = { nav.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterClick = { _, _, _, _, _ ->
                        // Register success → enter APP graph, clear AUTH graph
                        nav.navigate(Graph.APP) {
                            popUpTo(Graph.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onBackToLoginClick = { nav.popBackStack() }
                )
            }
        }

        /* ----------------------- APP GRAPH ---------------------- */
        navigation(startDestination = Routes.HOME, route = Graph.APP) {

            composable(Routes.HOME) {
                HomeScreen(
                    onOpenSettings = { nav.navigate(Routes.SETTINGS) },
                    onCreateNote   = { /* nav.navigate("createNote") */ },
                    onPressHome    = {
                        // Pop to existing HOME if present; otherwise no-op
                        val popped = nav.popBackStack(Routes.HOME, false)
                        if (!popped) {
                            nav.navigate(Routes.HOME) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { nav.popBackStack() },  // back → HOME (same graph)
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
}
