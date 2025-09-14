package ir.sharif.simplenote.app.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import ir.sharif.simplenote.feature.auth.ui.LoginScreen
import ir.sharif.simplenote.feature.auth.ui.OnboardingScreen
import ir.sharif.simplenote.feature.auth.ui.RegisterScreen
import ir.sharif.simplenote.feature.home.ui.HomeScreen
import ir.sharif.simplenote.feature.note.di.NotesGraph
import ir.sharif.simplenote.feature.note.presentation.NoteEditorViewModel
import ir.sharif.simplenote.feature.note.ui.EditNoteScreen
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

// ----------------------- APP GRAPH ----------------------
fun NavGraphBuilder.addAppGraph(nav: NavController) {
    navigation(startDestination = Routes.HOME, route = Graph.APP) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenSettings = { nav.navigate(Routes.SETTINGS) },
                onCreateNote   = { newId -> nav.navigate("${Routes.EDIT_NOTE}/$newId") },
                onOpenNote     = { id    -> nav.navigate("${Routes.EDIT_NOTE}/$id") },
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

        composable(
            route = "${Routes.EDIT_NOTE}/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val context = LocalContext.current
            val noteId = backStackEntry.arguments?.getInt("noteId")!!
            val vm: NoteEditorViewModel =
                viewModel(factory = NotesGraph.editorVmFactory(context, noteId))
            val ui by vm.ui.collectAsState()

            EditNoteScreen(
                title = ui.title,
                body = ui.content,
                lastEditedMillis = ui.lastEdited,
                onTitleChange = vm::onTitleChange,
                onBodyChange  = vm::onContentChange,
                onNavigateBack = { vm.onBack { nav.popBackStack() } },
                onConfirmDelete = { vm.delete { nav.popBackStack() } },
                onAutoSave = { vm.save { /* stay here */ } }
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
