package ir.sharif.simplenote.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ir.sharif.simplenote.feature.auth.ui.LoginScreen
import ir.sharif.simplenote.feature.auth.ui.OnboardingScreen
import ir.sharif.simplenote.feature.auth.ui.RegisterScreen
import ir.sharif.simplenote.feature.home.ui.HomeScreen
import ir.sharif.simplenote.feature.settings.ui.ChangePasswordScreen
import ir.sharif.simplenote.feature.settings.ui.EditProfileScreen
import ir.sharif.simplenote.feature.settings.ui.SettingsScreen

// ---------------------- AUTH GRAPH ----------------------
fun NavGraphBuilder.addOnboardingGraph(nav: NavController) {
    navigation(startDestination = Routes.ONBOARDING, route = Graph.AUTH) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onGetStartedClick = { nav.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ ->
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
                onRegisterClick = { firstName, lastName, username, email, password ->
                    nav.navigate(Graph.APP) {
                        popUpTo(Graph.AUTH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBackToLoginClick = { nav.popBackStack() }
            )
        }
    }
}

// ----------------------- APP GRAPH ----------------------
fun NavGraphBuilder.addAppGraph(nav: NavController) {
    navigation(startDestination = Routes.HOME, route = Graph.APP) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenSettings = { nav.navigate(Routes.SETTINGS) },
                onCreateNote   = { /* todo */ },
                onPressHome    = {
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
                onBack = { nav.popBackStack() },
                onNavigateToEditProfile = { nav.navigate(Routes.EDIT_PROFILE) },
                onNavigateToChangePassword = { nav.navigate(Routes.CHANGE_PASSWORD) }
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

// ----------------------- LOGIN-ONLY GRAPH ----------------------
fun NavGraphBuilder.addAuthGraph(nav: NavController) {
    navigation(startDestination = Routes.LOGIN, route = Graph.AUTH) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ ->
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
                    nav.navigate(Graph.APP) {
                        popUpTo(Graph.AUTH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBackToLoginClick = { nav.popBackStack() }
            )
        }
    }
}
