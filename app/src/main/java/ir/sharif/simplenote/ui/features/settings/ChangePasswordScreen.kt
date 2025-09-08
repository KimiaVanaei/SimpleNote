package ir.sharif.simplenote.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import ir.sharif.simplenote.ui.theme.TextStyles
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowRight
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordRetype by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Change Password",
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
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(onClick = onNavigateBack)
                            )
                            Text(
                                text = "Back",
                                style = TextStyles.textBaseMedium,
                                color = ColorPalette.PrimaryBase,
                                modifier = Modifier.clickable(onClick = onNavigateBack)
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.PrimaryBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(
                    text = "Please input your current password first",
                    style = TextStyles.textSm,
                    color = ColorPalette.PrimaryBase,
                    modifier = Modifier.align(Alignment.Start)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Current Password",
                        style = TextStyles.textBaseBold,
                        color = ColorPalette.NeutralBlack,
                    )
                    TextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        textStyle = TextStyles.textBase,
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = {
                            Text(
                                text = "*********",
                                color = ColorPalette.NeutralBaseGrey
                            )
                        },
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

                HorizontalDivider(
                    thickness = 1.dp,
                    color = ColorPalette.NeutralLightGrey,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Now, create your new password",
                    style = TextStyles.textSm,
                    color = ColorPalette.PrimaryBase,
                    modifier = Modifier.align(Alignment.Start)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "New Password",
                        style = TextStyles.textBaseBold,
                        color = ColorPalette.NeutralBlack,
                    )
                    TextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        textStyle = TextStyles.textBase,
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = {
                            Text(
                                text = "*********",
                                color = ColorPalette.NeutralBaseGrey
                            )
                        },
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
                    Text(
                        text = "Password should contain a-z, A-Z, 0-9",
                        style = TextStyles.text2Xs,
                        color = ColorPalette.NeutralBaseGrey,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Retype New Password",
                        style = TextStyles.textBaseBold,
                        color = ColorPalette.NeutralBlack,
                    )
                    TextField(
                        value = newPasswordRetype,
                        onValueChange = { newPasswordRetype = it },
                        textStyle = TextStyles.textBase,
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = {
                            Text(
                                text = "*********",
                                color = ColorPalette.NeutralBaseGrey
                            )
                        },
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

            Button(
                onClick = { /* TODO: Implement change password logic */ },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.PrimaryBase,
                    contentColor = ColorPalette.NeutralWhite
                ),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 54.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Submit New Password",
                    style = TextStyles.textBaseMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Heroicons.Outline.ArrowRight,
                    contentDescription = "Submit New Password",
                    modifier = Modifier.size(20.dp)
                        .offset(x = 80.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    SimpleNoteTheme {
        ChangePasswordScreen(onNavigateBack = {})
    }
}