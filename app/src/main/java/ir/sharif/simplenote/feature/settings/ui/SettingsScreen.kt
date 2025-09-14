package ir.sharif.simplenote.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Bell
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronRight
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.LockClosed
import ir.sharif.simplenote.R
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import ir.sharif.simplenote.core.ui.SettingsOption
import ir.sharif.simplenote.feature.settings.presentation.SettingsEvent
import ir.sharif.simplenote.feature.settings.presentation.SettingsViewModel
import ir.sharif.simplenote.core.ui.dialogs.LogoutDialog
import ir.sharif.simplenote.core.ui.dialogs.NotificationsDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    //navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile by viewModel.profile.collectAsState()

    // React to logout state
    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = TextStyles.textBaseMedium,
                        color = ColorPalette.NeutralBlack,
                        textAlign = TextAlign.Center,
                    )
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onBack)
                    ) {
                        Icon(
                            imageVector = Heroicons.Outline.ChevronLeft,
                            contentDescription = "Back",
                            tint = ColorPalette.PrimaryBase,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = TextStyles.textBaseMedium,
                            color = ColorPalette.PrimaryBase
                        )
                    }
                }
            )
        },
        bottomBar = {
            Text(
                text = "Sharif Notes v1.1",
                style = TextStyles.text2Xs,
                color = ColorPalette.NeutralBaseGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            val profile by viewModel.profile.collectAsState()

            ProfileSection(
                userName = profile.name,
                userEmail = profile.email,
                avatarUri = profile.avatarUri,
                onEditProfileClick = {
                    viewModel.onEvent(SettingsEvent.OnClickEditProfile)
                    onNavigateToEditProfile()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = ColorPalette.NeutralLightGrey,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // App Settings Section
            Text(
                text = "APP SETTINGS",
                style = TextStyles.textXs,
                color = ColorPalette.NeutralDarkGrey,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsOption(
                label = "Change Password",
                icon = Heroicons.Outline.LockClosed,
                onClick = {
                    viewModel.onEvent(SettingsEvent.OnClickChangePassword)
                    onNavigateToChangePassword()
                },
                rightContent = {
                    Icon(
                        Heroicons.Outline.ChevronRight,
                        contentDescription = "Arrow right",
                        tint = ColorPalette.NeutralDarkGrey,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            SettingsOption(
                label = "Notifications",
                icon = Heroicons.Outline.Bell,
                onClick = { viewModel.onEvent(SettingsEvent.OnOpenNotificationsDialog) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = ColorPalette.NeutralLightGrey,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingsOption(
                label = "Logout",
                labelColor = ColorPalette.ErrorBase,
                icon = R.drawable.logout,
                iconTint = ColorPalette.ErrorBase,
                onClick = { viewModel.onEvent(SettingsEvent.OnClickLogout) }
            )
        }
    }

    // Dialogs (visibility fully controlled by ViewModel)
    if (uiState.showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                viewModel.onEvent(SettingsEvent.ConfirmLogout)
            },
            onDismiss = { viewModel.onEvent(SettingsEvent.OnDismissLogout) }
        )
    }

    if (uiState.showNotificationsDialog) {
        NotificationsDialog(
            onDismiss = { viewModel.onEvent(SettingsEvent.OnDismissNotificationsDialog) },
            emailEnabled = uiState.emailNotificationsEnabled,
            onEmailToggled = { viewModel.onEvent(SettingsEvent.OnEmailNotificationsToggled(it)) },
            pushEnabled = uiState.pushNotificationsEnabled,
            onPushToggled = { viewModel.onEvent(SettingsEvent.OnPushNotificationsToggled(it)) }
        )
    }
}