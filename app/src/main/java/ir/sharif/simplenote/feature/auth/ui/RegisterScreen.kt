package ir.sharif.simplenote.feature.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import ir.sharif.simplenote.core.ui.components.LabeledTextField
import ir.sharif.simplenote.core.ui.components.LabeledPasswordField
import ir.sharif.simplenote.feature.auth.presentation.RegisterViewModel
import ir.sharif.simplenote.feature.auth.presentation.RegisterUiState
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, String) -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Register", style = TextStyles.text2XlBold, color = ColorPalette.NeutralBlack)
                        Text("And start taking notes", style = TextStyles.textBase, color = ColorPalette.NeutralDarkGrey)
                    }
                }

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
                            onValueChange = viewModel::onRetypeChange,
                            visible = state.showRetype,
                            onToggleVisibility = viewModel::toggleRetypeVisibility,
                            imeAction = ImeAction.Done
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel.register()
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

        if (state.success) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Status") },
                text = { Text("User Created") },
                confirmButton = {
                    TextButton(onClick = {
                        onBackToLoginClick()
                    }) {
                        Text("Ok")
                    }
                }
            )
        }

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.clearError()
                },
                title = { Text("Status") },
                text = { Text(state.error ?: "Unknown Error") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.clearError()
                    }) {
                        Text("Ok")
                    }
                }
            )
        }
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
