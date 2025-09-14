package ir.sharif.simplenote.feature.settings.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import ir.sharif.simplenote.feature.settings.presentation.SettingsEvent
import ir.sharif.simplenote.feature.settings.presentation.SettingsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import ir.sharif.simplenote.core.ui.components.LabeledPasswordField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    onSubmit: (current: String, new: String) -> Unit = { _, _ -> }
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var retypePassword by rememberSaveable { mutableStateOf("") }

    var showCurrent by rememberSaveable { mutableStateOf(false) }
    var showNew by rememberSaveable { mutableStateOf(false) }
    var showRetype by rememberSaveable { mutableStateOf(false) }

    val newMatches = newPassword.isNotEmpty() && newPassword == retypePassword
    val canSubmit = currentPassword.isNotBlank() && newMatches

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets,
                title = {
                    Text(
                        text = "Change Password",
                        style = TextStyles.textBaseMedium,
                        color = ColorPalette.NeutralBlack,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onNavigateBack)
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPalette.NeutralWhite,
                    titleContentColor = ColorPalette.NeutralBlack
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.NeutralWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Text(
                    text = "Please input your current password first",
                    style = TextStyles.textSm,
                    color = ColorPalette.PrimaryBase
                )

                // Current password
                LabeledPasswordField(
                    label = "Current Password",
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    visible = showCurrent,
                    onToggleVisibility = { showCurrent = !showCurrent },
                    imeAction = ImeAction.Next
                )

                Divider(thickness = 1.dp, color = ColorPalette.NeutralLightGrey)

                Text(
                    text = "Now, create your new password",
                    style = TextStyles.textSm,
                    color = ColorPalette.PrimaryBase
                )

                // New password
                LabeledPasswordField(
                    label = "New Password",
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    visible = showNew,
                    onToggleVisibility = { showNew = !showNew },
                    imeAction = ImeAction.Next
                )

                // Retype
                LabeledPasswordField(
                    label = "Retype New Password",
                    value = retypePassword,
                    onValueChange = { retypePassword = it },
                    visible = showRetype,
                    onToggleVisibility = { showRetype = !showRetype },
                    imeAction = ImeAction.Done,
                    onImeDone = {
                        if (canSubmit) onSubmit(currentPassword, newPassword)
                    }
                )

                if (retypePassword.isNotEmpty() && !newMatches) {
                    Text(
                        text = "Passwords do not match.",
                        style = TextStyles.text2Xs,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            // Submit
            Button(
                onClick = {
                    Log.d("ChangePasswordScreen", "Submit clicked with old=$currentPassword, new=$newPassword")
                    viewModel.onEvent(SettingsEvent.ChangePassword(currentPassword, newPassword))
                },
                enabled = canSubmit,
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.PrimaryBase,
                    contentColor = ColorPalette.NeutralWhite,
                    disabledContainerColor = ColorPalette.NeutralLightGrey,
                    disabledContentColor = ColorPalette.NeutralWhite
                ),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "Submit New Password", style = TextStyles.textBaseMedium)
            }
        }
    }

    if (uiState.changePasswordSuccess == true) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Status") },
            text = { Text(uiState.message ?: "Password Successfully Changed") },
            confirmButton = {
                TextButton(onClick = { onNavigateBack() }) {
                    Text("Ok")
                }
            }
        )
    }

    if (!uiState.changePasswordSuccess && uiState.changePasswordError != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.DismissChangePasswordError) },
            title = { Text("Status") },
            text = { Text(uiState.changePasswordError ?: "Failed at Changing Password") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(SettingsEvent.DismissChangePasswordError) }
                ) {
                    Text("Ok")
                }
            }
        )
    }
}
