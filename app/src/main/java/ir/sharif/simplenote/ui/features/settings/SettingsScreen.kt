package ir.sharif.simplenote.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.R
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import ir.sharif.simplenote.ui.theme.TextStyles
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Bell
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.LockClosed
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronRight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var emailNotificationsEnabled by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var showEditProfileScreen by remember { mutableStateOf(false) }
    var showChangePasswordScreen by remember { mutableStateOf(false) }

    val notificationStatus = when {
        emailNotificationsEnabled && pushNotificationsEnabled -> "All active"
        emailNotificationsEnabled -> "Email only"
        pushNotificationsEnabled -> "Push only"
        else -> "Off"
    }
    if (showEditProfileScreen) {
        EditProfileScreen(
            onNavigateBack = { showEditProfileScreen = false }
        )
    } else if (showChangePasswordScreen) {
        ChangePasswordScreen(
            onNavigateBack = { showChangePasswordScreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Settings",
                                style = TextStyles.textBaseMedium,
                                color = ColorPalette.NeutralBlack,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Icon(
                                    Heroicons.Outline.ChevronLeft,
                                    contentDescription = "Back",
                                    tint = ColorPalette.PrimaryBase,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Back",
                                    style = TextStyles.textBaseMedium,
                                    color = ColorPalette.PrimaryBase
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ColorPalette.NeutralWhite,
                        titleContentColor = ColorPalette.NeutralBlack
                    ),
                    modifier = Modifier
                        .height(60.dp)
                        .background(ColorPalette.NeutralWhite)
                )
            },
            bottomBar = {
                Text(
                    text = "Makarya Notes v1.1",
                    style = TextStyles.text2Xs,
                    color = ColorPalette.NeutralBaseGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPalette.PrimaryBackground)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                ProfileSection(
                    userName = "Taha Hamifar",
                    userEmail = "hamifar.taha@gmail.com",
                    onEditProfileClick = { showEditProfileScreen = true }
                )
                Spacer(modifier = Modifier.height(32.dp))
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

                // Settings Options
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsOption(
                        icon = Heroicons.Outline.LockClosed,
                        label = "Change Password",
                        onClick = { showChangePasswordScreen = true },
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
                        icon = R.drawable.textsize,
                        label = "Text Size",
                        onClick = { /* Handle text size click */ },
                        rightContent = {
                            Text(
                                text = "Medium",
                                style = TextStyles.text2Xs,
                                color = ColorPalette.NeutralDarkGrey
                            )
                        }
                    )
                    SettingsOption(
                        icon = Heroicons.Outline.Bell,
                        label = "Notifications",
                        onClick = {
                            showNotificationsDialog = true
                        }, // This will now show the dialog
                        rightContent = {
                            Text(
                                text = notificationStatus,
                                style = TextStyles.text2Xs,
                                color = ColorPalette.NeutralDarkGrey
                            )
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = ColorPalette.NeutralLightGrey,
                        thickness = 1.dp
                    )
                    SettingsOption(
                        icon = R.drawable.logout,
                        label = "Log Out",
                        onClick = { showLogoutDialog = true },
                        labelColor = ColorPalette.ErrorBase,
                        iconTint = ColorPalette.ErrorBase
                    )
                }
            }
        }
    }


    if (showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirm = {
                // TODO: Implement your log out logic here
                showLogoutDialog = false
            }
        )
    }

    if (showNotificationsDialog) {
        NotificationsDialog(
            onDismissRequest = { showNotificationsDialog = false },
            emailNotificationsEnabled = emailNotificationsEnabled,
            onEmailNotificationsToggled = { emailNotificationsEnabled = it },
            pushNotificationsEnabled = pushNotificationsEnabled,
            onPushNotificationsToggled = { pushNotificationsEnabled = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SimpleNoteTheme {
        SettingsScreen()
    }
}
