package ir.sharif.simplenote.feature.settings.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Check
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PencilSquare
import ir.sharif.simplenote.R
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import androidx.core.net.toUri
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    initialFullName: String = "Your name",
    initialEmail: String = "your.email@gmail.com",
    onSave: (fullName: String, email: String) -> Unit = { _, _ -> },
    onAvatarChange: (String?) -> Unit = {} // optional callback you already had
) {
    // CHANGE: get the shared VM and observe the live profile
    val vm: ir.sharif.simplenote.feature.settings.presentation.EditProfileViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
    val profile by vm.profile.collectAsState() // <— live data from repository

    // CHANGE: prefill once from the saved profile
    var initialized by rememberSaveable { mutableStateOf(false) }
    var fullName by rememberSaveable { mutableStateOf(initialFullName) }
    var emailAddress by rememberSaveable { mutableStateOf(initialEmail) }
    var avatarUri by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(profile, initialized) {
        if (!initialized) {
            fullName = profile.name.ifBlank { initialFullName }
            emailAddress = profile.email.ifBlank { initialEmail }
            avatarUri = profile.avatarUri // could be null
            initialized = true
        }
    }

    val context = LocalContext.current

    // Photo Picker
    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            val picked = uri?.toString()
            avatarUri = picked
            vm.updateAvatar(picked)          // CHANGE: persist avatar
            onAvatarChange(picked)           // optional external callback
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        style = TextStyles.textBaseMedium,
                        color = ColorPalette.NeutralBlack,
                        textAlign = TextAlign.Center,
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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.NeutralWhite)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ----- Avatar + Change/Remove -----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Render picked bitmap if available; otherwise show placeholder
                    val bitmap = remember(avatarUri) {
                        avatarUri?.let {
                            try {
                                context.contentResolver.openInputStream(it.toUri()).use { stream ->
                                    stream?.let { BitmapFactory.decodeStream(it) }
                                }
                            } catch (_: Throwable) { null }
                        }
                    }

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile_picture),
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = {
                                pickPhoto.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
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
                                tint = ColorPalette.PrimaryBase,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Change Image", style = TextStyles.textBaseMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ----- Full Name -----
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

            // ----- Email -----
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
                text = "Changing email address information means you need to re-login to the app.",
                style = TextStyles.text2Xs,
                color = ColorPalette.NeutralDarkGrey,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ----- Save -----
            Button(
                onClick = {
                    vm.save(fullName, emailAddress) {
                        onNavigateBack()          // ← single navigation back
                    }
                },
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
                Icon(
                    Heroicons.Outline.Check,
                    contentDescription = "Save Changes",
                    modifier = Modifier.size(20.dp)
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
