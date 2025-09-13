package ir.sharif.simplenote.core.ui.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Trash
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import java.text.SimpleDateFormat
import java.util.*

/**
 * A slim bar:
 *  ┌────────────────────────────────────────────┬─────┐
 *  │ Last edited on 19.30                       │ 🗑 │  (purple block)
 *  └────────────────────────────────────────────┴─────┘
 */
@Composable
fun TaskBar(
    lastEditedMillis: Long? = null,
    lastEditedText: String? = null,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    deleteBlockWidth: Dp = 56.dp,
) {
    val text = remember(lastEditedMillis, lastEditedText) {
        lastEditedText ?: lastEditedMillis?.let { "Last edited on ${formatTimeHHmm(it)}" } ?: ""
    }

    Surface(
        color = ColorPalette.NeutralWhite,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: label
            Text(
                text = text,
                style = TextStyles.textBaseMedium,
                color = ColorPalette.NeutralBlack,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )

            // Right: purple delete block
            Box(
                modifier = Modifier
                    .width(deleteBlockWidth)
                    .fillMaxHeight()
                    .background(ColorPalette.PrimaryBase)
                    .clickable(onClick = onDeleteClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Heroicons.Outline.Trash,
                    contentDescription = "Delete note",
                    tint = Color.White
                )
            }
        }
    }
}

/* ---------- small helper ---------- */
private fun formatTimeHHmm(millis: Long): String {
    val fmt = SimpleDateFormat("HH.mm", Locale.getDefault())
    return fmt.format(Date(millis))
}

/* ---------- preview ---------- */
@Preview(showBackground = true)
@Composable
private fun TaskBarPreview() {
    TaskBar(
        lastEditedText = "Last edited on 19.30",
        onDeleteClick = {}
    )
}
