// file: ir/sharif/simplenote/core/ui/bars/TabBar.kt
package ir.sharif.simplenote.core.ui.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Home
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Cog6Tooth
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.Home

enum class BottomTab { Home, Settings }

@Composable
fun TabBar(
    selected: BottomTab,
    onHomeClick: () -> Unit,
    onFabClick: () -> Unit,
    onSettingsClick: () -> Unit,
    barHeight: Dp = 110.dp,
    cornerRadius: Dp = 16.dp,
    fabSize: Dp = 65.dp,
    fabElevation: Dp = 8.dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // Bottom bar container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .align(Alignment.BottomCenter)
                .shadow(
                    8.dp,
                    shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
                    clip = false
                )
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(ColorPalette.NeutralWhite)
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
        ) {
            // Top divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.TopCenter)
                    .background(ColorPalette.NeutralLightGrey)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TabItem(
                    label = "Home",
                    selected = selected == BottomTab.Home,
                    icon = { Icon(Heroicons.Solid.Home, contentDescription = "Home") },
                    onClick = onHomeClick
                )

                Spacer(Modifier.width(fabSize)) // reserve space for center FAB

                TabItem(
                    label = "Settings",
                    selected = selected == BottomTab.Settings,
                    icon = { Icon(Heroicons.Outline.Cog6Tooth, contentDescription = "Settings") },
                    onClick = onSettingsClick
                )
            }
        }

        // Center FAB (hovering)
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = ColorPalette.PrimaryBase,
            contentColor = ColorPalette.NeutralWhite,
            shape = CircleShape,
            modifier = Modifier
                .size(fabSize)
                .align(Alignment.Center)
                .offset(y = -54.dp)
                .shadow(fabElevation, CircleShape, clip = false)
        ) {
            Icon(Heroicons.Outline.Plus, contentDescription = "Add", modifier = Modifier.size(35.dp))
        }
    }
}

@Composable
private fun TabItem(
    label: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val color = if (selected) ColorPalette.PrimaryBase else ColorPalette.NeutralDarkGrey
    Column(
        modifier = Modifier
            .widthIn(min = 72.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides color) {
            icon()
        }
        Spacer(Modifier.height(8.dp))
        Text(text = label, style = TextStyles.text2Xs, color = color)
    }
}

@Preview(showBackground = true)
@Composable
private fun TabBarInScaffoldPreview() {
    Scaffold(
        bottomBar = {
            Box(Modifier.fillMaxWidth()) {
                TabBar(
                    selected = BottomTab.Settings,
                    onHomeClick = {},
                    onFabClick = {},
                    onSettingsClick = {},
                    modifier = Modifier.align(Alignment.BottomCenter) // now works in preview too
                )
            }
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(ColorPalette.PrimaryBackground)
        )
    }
}
