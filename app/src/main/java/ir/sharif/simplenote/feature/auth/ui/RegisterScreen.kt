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
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.ChevronLeft

// ---------------- UI STATE ----------------
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val retypePassword: String = "",
    val showPassword: Boolean = false,
    val showRetype: Boolean = false
)

// ---------------- VIEWMODEL ----------------
class RegisterViewModel : androidx.lifecycle.ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onFirstNameChange(value: String) { uiState = uiState.copy(firstName = value) }
    fun onLastNameChange(value: String) { uiState = uiState.copy(lastName = value) }
    fun onUsernameChange(value: String) { uiState = uiState.copy(username = value) }
    fun onEmailChange(value: String) { uiState = uiState.copy(email = value) }
    fun onPasswordChange(value: String) { uiState = uiState.copy(password = value) }
    fun onRetypePasswordChange(value: String) { uiState = uiState.copy(retypePassword = value) }
    fun togglePasswordVisibility() { uiState = uiState.copy(showPassword = !uiState.showPassword) }
    fun toggleRetypeVisibility() { uiState = uiState.copy(showRetype = !uiState.showRetype) }
}

// ---------------- SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, String) -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel()
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onBackToLoginClick)
                    ) {
                        Icon(
                            imageVector = Heroicons.Solid.ChevronLeft,
                            contentDescription = null,
                            tint = ColorPalette.PrimaryBase,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Back to Login",
                            style = TextStyles.textBaseMedium,
                            color = ColorPalette.PrimaryBase
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPalette.NeutralWhite
                )
            )
        },
        containerColor = ColorPalette.NeutralWhite
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register", style = TextStyles.text2XlBold, color = ColorPalette.NeutralBlack)
                Text("And start taking notes", style = TextStyles.textBase, color = ColorPalette.NeutralDarkGrey)
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(40.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LabeledTextField(
                            label = "First Name",
                            value = state.firstName,
                            onValueChange = viewModel::onFirstNameChange,
                            placeholder = "Example: Taha",
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )

                        LabeledTextField(
                            label = "Last Name",
                            value = state.lastName,
                            onValueChange = viewModel::onLastNameChange,
                            placeholder = "Example: Hamifar",
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )

                        LabeledTextField(
                            label = "Username",
                            value = state.username,
                            onValueChange = viewModel::onUsernameChange,
                            placeholder = "Example: @HamifarTaha",
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )

                        LabeledTextField(
                            label = "Email Address",
                            value = state.email,
                            onValueChange = viewModel::onEmailChange,
                            placeholder = "Example: hamifar.taha@gmail.com",
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                        LabeledPasswordField(
                            label = "Password",
                            value = state.password,
                            onValueChange = viewModel::onPasswordChange,
                            visible = state.showPassword,
                            onToggleVisibility = viewModel::togglePasswordVisibility,
                            imeAction = ImeAction.Next
                        )

                        LabeledPasswordField(
                            label = "Retype Password",
                            value = state.retypePassword,
                            onValueChange = viewModel::onRetypePasswordChange,
                            visible = state.showRetype,
                            onToggleVisibility = viewModel::toggleRetypeVisibility,
                            imeAction = ImeAction.Done
                        )
                    }
                }

                item {
                    Button(
                        onClick = {
                            onRegisterClick(
                                state.firstName,
                                state.lastName,
                                state.username,
                                state.email,
                                state.password
                            )
                        },
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

                    Spacer(modifier = Modifier.height(16.dp))

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
}

// ---------------- COMPONENTS ----------------
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
                .height(54.dp)
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
        Text(text = label, style = TextStyles.textBaseMedium, color = ColorPalette.NeutralBlack)
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
                .height(54.dp)
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
