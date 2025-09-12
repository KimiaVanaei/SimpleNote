package ir.sharif.simplenote.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Check
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PencilSquare
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.ArrowRight
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = ColorPalette.NeutralWhite
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(ColorPalette.NeutralWhite)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Let's Login",
                        style = TextStyles.text2XlBold,
                        color = ColorPalette.NeutralBlack
                    )
                    Text(
                        text = "And notes your idea",
                        style = TextStyles.textBase,
                        color = ColorPalette.NeutralDarkGrey
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Email
                        LabeledTextField(
                            label = "Email Address",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "Example: johndoe@gmail.com",
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                        // Password
                        LabeledPasswordField(
                            label = "Password",
                            value = password,
                            onValueChange = { password = it },
                            visible = showPassword,
                            onToggleVisibility = { showPassword = !showPassword },
                            imeAction = ImeAction.Done
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Login Button
                        Button(
                            onClick = { onLoginClick(email, password) },
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorPalette.PrimaryBase,
                                contentColor = ColorPalette.NeutralWhite
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Login", style = TextStyles.textBaseMedium, color = ColorPalette.NeutralWhite)
                                Icon(
                                    imageVector = Heroicons.Solid.ArrowRight,
                                    contentDescription = null,
                                    tint = ColorPalette.NeutralWhite,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Divider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Divider(modifier = Modifier.weight(1f), color = ColorPalette.NeutralLightGrey)
                            Text("  Or  ", color = ColorPalette.NeutralDarkGrey, style = TextStyles.text2XsMedium)
                            Divider(modifier = Modifier.weight(1f), color = ColorPalette.NeutralLightGrey)
                        }

                        // Register Button
                        Button(
                            onClick = onRegisterClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = ColorPalette.PrimaryBase
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Text(
                                text = "Don’t have any account? Register here",
                                style = TextStyles.textBaseMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onImeDone: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = TextStyles.textBaseMedium, color = ColorPalette.NeutralBlack)
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyles.textBase,
            placeholder = { Text(placeholder, color = ColorPalette.NeutralBaseGrey) },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onDone = { onImeDone() }),
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
    }
}

@Composable
private fun LabeledPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
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
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (visible) "Hide password" else "Show password",
                        tint = ColorPalette.NeutralDarkGrey
                    )
                }
            },
            placeholder = { Text("*********", color = ColorPalette.NeutralBaseGrey) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onDone = { onImeDone() }),
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
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    SimpleNoteTheme {
        LoginScreen(onLoginClick = { _, _ -> }, onRegisterClick = {})
    }
}
