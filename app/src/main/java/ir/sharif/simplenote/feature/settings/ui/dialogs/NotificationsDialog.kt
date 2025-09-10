package ir.sharif.simplenote.feature.settings.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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

/**
 * Controlled dialog: all state comes from the caller.
 */
@Composable
fun NotificationsDialog(
    onDismiss: () -> Unit,
    emailEnabled: Boolean,
    onEmailToggled: (Boolean) -> Unit,
    pushEnabled: Boolean,
    onPushToggled: (Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
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
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                color = ColorPalette.NeutralWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = { /* consume */ })
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Close button row
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
                                .clickable(onClick = onDismiss),
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

                    Spacer(Modifier.height(24.dp))

                    NotificationToggle(
                        label = "Email Notifications",
                        isChecked = emailEnabled,
                        onCheckedChange = onEmailToggled
                    )

                    Spacer(Modifier.height(16.dp))

                    NotificationToggle(
                        label = "Push Notifications",
                        isChecked = pushEnabled,
                        onCheckedChange = onPushToggled
                    )

                    Spacer(Modifier.height(25.dp))
                }
            }
        }
    }
}

@Composable
private fun NotificationToggle(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = TextStyles.textBase, color = ColorPalette.NeutralBlack)
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
private fun NotificationsDialogPreview() {
    SimpleNoteTheme {
        NotificationsDialog(
            onDismiss = {},
            emailEnabled = true,
            onEmailToggled = {},
            pushEnabled = false,
            onPushToggled = {}
        )
    }
}
