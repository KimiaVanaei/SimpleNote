package ir.sharif.simplenote.feature.settings.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.sharif.simplenote.core.ui.SettingsOption
import ir.sharif.simplenote.feature.settings.presentation.SettingsEvent
import ir.sharif.simplenote.feature.settings.presentation.SettingsViewModel
import ir.sharif.simplenote.feature.settings.ui.dialogs.LogoutDialog
import ir.sharif.simplenote.feature.settings.ui.dialogs.NotificationsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            // Header / Profile
            ProfileSection(
                onEditProfileClick = {
                    viewModel.onEvent(SettingsEvent.OnClickEditProfile)
                    onNavigateToEditProfile()
                }
            )

            Spacer(Modifier.height(16.dp))

            // --- General / Account section ---
            SettingsOption(
                title = "Edit Profile",
                onClick = {
                    viewModel.onEvent(SettingsEvent.OnClickEditProfile)
                    onNavigateToEditProfile()
                }
            )

            SettingsOption(
                title = "Change Password",
                onClick = {
                    viewModel.onEvent(SettingsEvent.OnClickChangePassword)
                    onNavigateToChangePassword()
                }
            )

            // --- Preferences ---
            // Open a dialog that controls Email + Push toggles (kept in ViewModel)
            SettingsOption(
                title = "Notifications",
                onClick = { viewModel.onEvent(SettingsEvent.OnOpenNotificationsDialog) }
            )

            SettingsOption(
                title = "Dark Mode",
                rightContent = {
                    Switch(
                        checked = state.darkModeEnabled,
                        onCheckedChange = { viewModel.onEvent(SettingsEvent.OnToggleDarkMode) }
                    )
                }
            )

            // --- About / Logout ---
            SettingsOption(
                title = "Logout",
                onClick = { viewModel.onEvent(SettingsEvent.OnClickLogout) }
            )

            SettingsOption(
                title = "App Version",
                rightContent = { Text(text = "v1.1", style = MaterialTheme.typography.bodyMedium) },
                onClick = {} // info row
            )
        }
    }

    // Dialogs (visibility fully controlled by ViewModel)
    if (state.showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                // TODO: trigger real logout via VM -> repo
                viewModel.onEvent(SettingsEvent.OnDismissLogout)
            },
            onDismiss = { viewModel.onEvent(SettingsEvent.OnDismissLogout) }
        )
    }

    if (state.showNotificationsDialog) {
        NotificationsDialog(
            onDismiss = { viewModel.onEvent(SettingsEvent.OnDismissNotificationsDialog) },
            emailEnabled = state.emailNotificationsEnabled,
            onEmailToggled = { viewModel.onEvent(SettingsEvent.OnEmailNotificationsToggled(it)) },
            pushEnabled = state.pushNotificationsEnabled,
            onPushToggled = { viewModel.onEvent(SettingsEvent.OnPushNotificationsToggled(it)) }
        )
    }

}
