package ir.sharif.simplenote.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles
import ir.sharif.simplenote.feature.auth.presentation.LoginUiState
import ir.sharif.simplenote.feature.auth.presentation.LoginViewModel
import ir.sharif.simplenote.core.ui.components.LabeledTextField
import ir.sharif.simplenote.core.ui.components.LabeledPasswordField
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state: LoginUiState = viewModel.uiState

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
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Let's Login", style = TextStyles.text2XlBold, color = ColorPalette.NeutralBlack)
                    Text("And notes your idea", style = TextStyles.textBase, color = ColorPalette.NeutralDarkGrey)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Email
                        LabeledTextField(
                            label = "Email Address",
                            value = state.email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            placeholder = "Example: johndoe@gmail.com",
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                        // Password
                        LabeledPasswordField(
                            label = "Password",
                            value = state.password,
                            onValueChange = { viewModel.onPasswordChange(it) },
                            visible = state.showPassword,
                            onToggleVisibility = { viewModel.togglePasswordVisibility() },
                            imeAction = ImeAction.Done
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Login Button
                        Button(
                            onClick = { viewModel.login() },
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
                                    "Login",
                                    style = TextStyles.textBaseMedium,
                                    color = ColorPalette.NeutralWhite,
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

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            state.error?.let { err ->
                Text(
                    text = err,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }

            if (state.success) {
                LaunchedEffect(Unit) {
                    onLoginClick(state.email, state.password)
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    SimpleNoteTheme {
        LoginScreen(onLoginClick = { _, _ -> }, onRegisterClick = {})
    }
}
