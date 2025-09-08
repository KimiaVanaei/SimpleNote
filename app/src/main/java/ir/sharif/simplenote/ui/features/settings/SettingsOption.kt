package ir.sharif.simplenote.ui.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.ui.theme.TextStyles
import ir.sharif.simplenote.ui.theme.ColorPalette

@Composable
fun CustomIcon(
    icon: Any,
    contentDescription: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is Int -> Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = tint,
            modifier = modifier
        )
        is ImageVector -> Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = modifier
        )
        else -> throw IllegalArgumentException("Unsupported icon type")
    }
}

@Composable
fun SettingsOption(
    modifier: Modifier = Modifier,
    icon: Any, // <-- now supports both Int and ImageVector
    label: String,
    onClick: () -> Unit,
    labelColor: Color = ColorPalette.NeutralBlack,
    iconTint: Color = ColorPalette.NeutralBlack,
    rightContent: @Composable (() -> Unit) = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomIcon(
            icon = icon,
            contentDescription = "Icon",
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            style = TextStyles.textBaseMedium,
            color = labelColor,
            modifier = Modifier.weight(1f)
        )
        rightContent()
    }
}
