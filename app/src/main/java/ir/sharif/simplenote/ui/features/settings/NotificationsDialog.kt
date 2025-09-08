package ir.sharif.simplenote.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.XMark
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import ir.sharif.simplenote.ui.theme.TextStyles

@Composable
fun NotificationsDialog(
    onDismissRequest: () -> Unit,
    emailNotificationsEnabled: Boolean,
    onEmailNotificationsToggled: (Boolean) -> Unit,
    pushNotificationsEnabled: Boolean,
    onPushNotificationsToggled: (Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .clickable(onClick = onDismissRequest),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                color = ColorPalette.NeutralWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 24.dp)
                    .clickable(onClick = { /* Do nothing */ })
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ColorPalette.NeutralLightGrey)
                                .clickable(onClick = onDismissRequest),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Heroicons.Solid.XMark,
                                contentDescription = "Close",
                                tint = ColorPalette.NeutralDarkGrey,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    NotificationToggle(
                        label = "Email Notifications",
                        isChecked = emailNotificationsEnabled,
                        onCheckedChange = onEmailNotificationsToggled
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NotificationToggle(
                        label = "Push Notifications",
                        isChecked = pushNotificationsEnabled,
                        onCheckedChange = onPushNotificationsToggled
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationToggle(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // Set a fixed height for better spacing
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyles.textBase,
            color = ColorPalette.NeutralBlack
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorPalette.PrimaryBase,
                checkedTrackColor = ColorPalette.PrimaryLight,
                uncheckedThumbColor = ColorPalette.NeutralWhite,
                uncheckedTrackColor = ColorPalette.NeutralBaseGrey
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsDialogPreview() {
    SimpleNoteTheme {
        NotificationsDialog(
            onDismissRequest = {},
            emailNotificationsEnabled = true,
            onEmailNotificationsToggled = {},
            pushNotificationsEnabled = false,
            onPushNotificationsToggled = {}
        )
    }
}