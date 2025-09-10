package ir.sharif.simplenote.feature.settings.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import ir.sharif.simplenote.ui.theme.TextStyles

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        containerColor = ColorPalette.NeutralWhite,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Log Out",
                style = TextStyles.textLgBold,
                color = ColorPalette.NeutralBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Are you sure you want to log out from the application?",
                style = TextStyles.textSmMedium,
                color = ColorPalette.NeutralDarkGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        // Primary action
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.PrimaryBase,
                    contentColor = ColorPalette.NeutralWhite
                ),
                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(text = "Yes", style = TextStyles.textSmBold)
            }
        },
        // Secondary action (cancel) belongs here
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(100.dp),
                border = BorderStroke(1.dp, ColorPalette.PrimaryBase),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = ColorPalette.PrimaryBase
                ),
                contentPadding = PaddingValues(horizontal = 30.dp, vertical = 12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(text = "Cancel", style = TextStyles.textSmBold)
            }
        },
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun LogoutDialogPreview() {
    SimpleNoteTheme {
        LogoutDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
