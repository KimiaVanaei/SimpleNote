package ir.sharif.simplenote.feature.settings.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.R
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import ir.sharif.simplenote.ui.theme.TextStyles
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PencilSquare

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    initialFullName: String = "Taha Hamifar",
    initialEmail: String = "hamifar.taha@gmail.com",
    onSave: (fullName: String, email: String) -> Unit = { _, _ -> }
) {
    var fullName by rememberSaveable { mutableStateOf(initialFullName) }
    var emailAddress by rememberSaveable { mutableStateOf(initialEmail) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ColorPalette.PrimaryBase
                        )
                    }
                },
                title = {
                    Text(
                        text = "Edit Profile",
                        style = TextStyles.textBaseMedium,
                        color = ColorPalette.NeutralBlack,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.PrimaryBackground)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar + Change image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture),
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        onClick = { /* TODO: pick image */ },
                        shape = RoundedCornerShape(100.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ColorPalette.NeutralWhite,
                            contentColor = ColorPalette.PrimaryBase
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            Heroicons.Outline.PencilSquare,
                            contentDescription = "Change Image",
                            tint = ColorPalette.PrimaryBase,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Change Image", style = TextStyles.textBaseMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name
            Text(
                text = "Full Name",
                style = TextStyles.textBaseBold,
                color = ColorPalette.NeutralBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                textStyle = TextStyles.textSm,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
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
                        width = 2.dp,
                        color = ColorPalette.NeutralLightGrey,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            Text(
                text = "Email Address",
                style = TextStyles.textBaseBold,
                color = ColorPalette.NeutralBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = emailAddress,
                onValueChange = { emailAddress = it },
                textStyle = TextStyles.textSm,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
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
                        width = 2.dp,
                        color = ColorPalette.NeutralLightGrey,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Text(
                text = "Changing email address requires re-login to the app.",
                style = TextStyles.text2Xs,
                color = ColorPalette.NeutralDarkGrey,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save
            Button(
                onClick = { onSave(fullName, emailAddress) },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.PrimaryBase,
                    contentColor = ColorPalette.NeutralWhite
                ),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                // Centered content without layout hacks
                Text(text = "Save Changes", style = TextStyles.textBaseMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    SimpleNoteTheme {
        EditProfileScreen(onNavigateBack = {})
    }
}
