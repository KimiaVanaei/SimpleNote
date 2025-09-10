package ir.sharif.simplenote.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Reusable settings row.
 *
 * title        -> main text (required)
 * icon         -> optional leading icon (ImageVector)
 * rightContent -> optional trailing composable (e.g., Switch)
 * onClick      -> whole row click (optional)
 */
@Composable
fun SettingsOption(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    rightContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.width(12.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        if (rightContent != null) {
            Spacer(Modifier.width(12.dp))
            rightContent()
        }
    }
}
