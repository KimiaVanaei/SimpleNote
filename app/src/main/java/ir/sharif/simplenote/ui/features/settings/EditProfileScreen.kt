package ir.sharif.simplenote.ui.features.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.R
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import ir.sharif.simplenote.ui.theme.TextStyles
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Check
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PencilSquare

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("Taha Hamifar") }
    var emailAddress by remember { mutableStateOf("hamifar.taha@gmail.com") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Edit Profile",
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
                                text = "Settings",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.PrimaryBackground)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
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
                        onClick = { /* Handle change image click */ },
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(1.dp, ColorPalette.PrimaryBase),
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
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Change Image",
                            style = TextStyles.textBaseMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Input
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

            // Email Address Input
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
                colors = TextFieldDefaults.colors( // Corrected line
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
                text = "Changing email address information means you need to re-login to the apps.",
                style = TextStyles.text2Xs,
                color = ColorPalette.NeutralDarkGrey,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Handle save changes click */ },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.PrimaryBase,
                    contentColor = ColorPalette.NeutralWhite
                ),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    Heroicons.Outline.Check,
                    contentDescription = "Save Changes",
                    modifier = Modifier.size(20.dp)
                        .offset(x = (-110).dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Changes",
                    style = TextStyles.textBaseMedium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    SimpleNoteTheme {
        EditProfileScreen(onNavigateBack = {})
    }
}