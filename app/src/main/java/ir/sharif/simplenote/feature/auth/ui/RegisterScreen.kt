package ir.sharif.simplenote.feature.auth.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.ArrowRight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, String) -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var retypePassword by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showRetype by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {}, // 👈 بدون تیتر
                navigationIcon = {
                    Text(
                        text = "< Back to Login",
                        style = TextStyles.textSm,
                        color = ColorPalette.PrimaryBase,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { onBackToLoginClick() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPalette.NeutralWhite
                )
            )
        },
        containerColor = ColorPalette.NeutralWhite
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Register", style = TextStyles.text2XlBold, color = ColorPalette.NeutralBlack)
                    Text("And start taking notes", style = TextStyles.textBase, color = ColorPalette.NeutralDarkGrey)
                }
            }

            item {
                LabeledTextField(
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "Example: Taha",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            }

            item {
                LabeledTextField(
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Example: Hamifar",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            }
            
            item {
                LabeledTextField(
                    label = "Username",
                    value = username,
                    onValueChange = { username = it },
                    placeholder = "Example: @HamifarTaha",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            }

            item {
                LabeledTextField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Example: hamifar.taha@gmail.com",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            }

            item {
                LabeledPasswordField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    visible = showPassword,
                    onToggleVisibility = { showPassword = !showPassword },
                    imeAction = ImeAction.Next
                )
            }

            item {
                LabeledPasswordField(
                    label = "Retype Password",
                    value = retypePassword,
                    onValueChange = { retypePassword = it },
                    visible = showRetype,
                    onToggleVisibility = { showRetype = !showRetype },
                    imeAction = ImeAction.Done
                )
            }

            item {
                Button(
                    onClick = { onRegisterClick(firstName, lastName, username, email, password) },
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Register",
                            style = TextStyles.textBaseMedium,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = Heroicons.Solid.ArrowRight,
                            contentDescription = null,
                            tint = ColorPalette.NeutralWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Already have an account? Login here",
                    style = TextStyles.textSm,
                    color = ColorPalette.PrimaryBase,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onBackToLoginClick() }
                )
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
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
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
                .border(1.dp, ColorPalette.NeutralBaseGrey, RoundedCornerShape(12.dp))
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
                    Icon(imageVector = icon, contentDescription = null, tint = ColorPalette.NeutralDarkGrey)
                }
            },
            placeholder = { Text("*********", color = ColorPalette.NeutralBaseGrey) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
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
                .border(1.dp, ColorPalette.NeutralBaseGrey, RoundedCornerShape(12.dp))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    SimpleNoteTheme {
        RegisterScreen(
            onRegisterClick = { _, _, _, _, _ -> },
            onBackToLoginClick = {}
        )
    }
}
