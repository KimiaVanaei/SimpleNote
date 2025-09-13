package ir.sharif.simplenote.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import ir.sharif.simplenote.feature.auth.ui.LoginScreen
import ir.sharif.simplenote.feature.auth.ui.OnboardingScreen
import ir.sharif.simplenote.feature.auth.ui.RegisterScreen
import ir.sharif.simplenote.feature.home.ui.HomeScreen
import ir.sharif.simplenote.feature.settings.ui.ChangePasswordScreen
import ir.sharif.simplenote.feature.settings.ui.EditProfileScreen
import ir.sharif.simplenote.feature.settings.ui.SettingsScreen

// EDITOR wiring
import ir.sharif.simplenote.feature.note.di.NotesGraph
import ir.sharif.simplenote.feature.note.presentation.NoteEditorViewModel
import ir.sharif.simplenote.feature.note.ui.EditNoteScreen

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
    const val HOME            = "home"
    const val SETTINGS        = "settings"
    const val EDIT_PROFILE    = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"

    // NEW for Option B: open editor by id
    const val EDIT_NOTE = "edit_note"
}

/**
 * Same behavior as before:
 * - Start in AUTH graph (Onboarding/Login/Register) unless you mark user onboarded/logged in.
 * - APP graph starts at HOME.
 * - Added EDIT_NOTE/{noteId} inside APP graph for Option B.
 */
@Composable
fun AppNavHost(
    nav: NavHostController,
    isOnboarded: Boolean = false,
    isLoggedIn: Boolean = false
) {
    val startGraph = when {
        !isOnboarded -> Graph.AUTH
        !isLoggedIn  -> Graph.AUTH
        else         -> Graph.APP
    }

    val context = LocalContext.current

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
                // HOME now supports Option B callbacks:
                // - onCreateNote(newId)  -> navigate to EDIT_NOTE/newId
                // - onOpenNote(id)       -> navigate to EDIT_NOTE/id
                HomeScreen(
                    onOpenSettings = { nav.navigate(Routes.SETTINGS) },
                    onCreateNote   = { newId -> nav.navigate("${Routes.EDIT_NOTE}/$newId") },
                    onOpenNote     = { id    -> nav.navigate("${Routes.EDIT_NOTE}/$id") },
                    onPressHome    = {
                        // Pop to existing HOME if present; otherwise restore/launch single top
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
                    onNavigateBack = { vm.onBack { nav.popBackStack() } },           // ✅ save-or-delete on back
                    onConfirmDelete = { vm.delete { nav.popBackStack() } },
                    onAutoSave = { vm.save { /* stay here */ } }
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
}
