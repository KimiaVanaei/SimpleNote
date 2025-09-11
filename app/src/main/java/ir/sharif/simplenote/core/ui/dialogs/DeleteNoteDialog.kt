package ir.sharif.simplenote.core.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Trash
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.XMark
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles

@Composable
fun DeleteNoteDialog(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        // Full-screen box to draw scrim and place the sheet at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            // Scrim (click to dismiss)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = onDismiss),
            )

            // Bottom sheet
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = ColorPalette.NeutralWhite,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)) {

                    // Header row: title + close
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Want to Delete this Note?",
                            style = TextStyles.textBaseMedium,
                            color = ColorPalette.NeutralBlack,
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(ColorPalette.NeutralLightGrey)
                                .clickable(onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Heroicons.Solid.XMark,
                                contentDescription = "Close",
                                tint = ColorPalette.NeutralDarkGrey
                            )
                        }
                    }

                    // Divider
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ColorPalette.NeutralLightGrey)
                    )
                    Spacer(Modifier.height(12.dp))

                    // Destructive action row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable(onClick = onConfirmDelete)
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Heroicons.Outline.Trash,
                            contentDescription = "Delete",
                            tint = ColorPalette.ErrorBase
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Delete Note",
                            style = TextStyles.textBaseMedium,
                            color = ColorPalette.ErrorBase
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DeleteNoteDialogPreview() {
    SimpleNoteTheme {
        DeleteNoteDialog(
            onDismiss = {},
            onConfirmDelete = {}
        )
    }
}
