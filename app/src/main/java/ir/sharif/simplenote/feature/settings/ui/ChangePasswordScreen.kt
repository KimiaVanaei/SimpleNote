package ir.sharif.simplenote.feature.settings.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    onSubmit: (current: String, new: String) -> Unit = { _, _ -> }
) {
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
                    helper = "Password should contain a-z, A-Z, 0-9",
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
                onClick = { onSubmit(currentPassword, newPassword) },
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
}

@Composable
private fun LabeledPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    helper: String? = null,
    imeAction: ImeAction,
    onImeDone: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = TextStyles.textBaseBold, color = ColorPalette.NeutralBlack)
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyles.textBase,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val desc = if (visible) "Hide password" else "Show password"
                IconButton(onClick = onToggleVisibility) {
                    Icon(imageVector = icon, contentDescription = desc, tint = ColorPalette.NeutralDarkGrey)
                }
            },
            placeholder = { Text(text = "*********", color = ColorPalette.NeutralBaseGrey) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeDone() }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.NeutralWhite,
                unfocusedContainerColor = ColorPalette.NeutralWhite,
                disabledContainerColor = ColorPalette.NeutralWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = ColorPalette.NeutralBaseGrey,
                    shape = RoundedCornerShape(12.dp)
                )
        )
        if (helper != null) {
            Text(
                text = helper,
                style = TextStyles.text2Xs,
                color = ColorPalette.NeutralBaseGrey,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordScreenPreview() {
    SimpleNoteTheme {
        ChangePasswordScreen(onNavigateBack = {})
    }
}
