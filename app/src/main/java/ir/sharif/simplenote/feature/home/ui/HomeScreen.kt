package ir.sharif.simplenote.feature.home.ui
/*
* needs repository and db codes
*
* will be completed later
* */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.ui.bars.BottomTab
import ir.sharif.simplenote.core.ui.bars.TabBar

@Composable
fun HomeScreen(
    onOpenSettings: () -> Unit,
    onCreateNote: () -> Unit,
    onPressHome: () -> Unit, // 👈 handle Home button explicitly
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.PrimaryBackground)
    ) {
        // Only empty state for now
        StartJourney(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 110.dp) // reserve TabBar height
        )

        // Pin TabBar to bottom
        TabBar(
            selected = BottomTab.Home,
            onHomeClick = onPressHome,
            onFabClick = onCreateNote,
            onSettingsClick = onOpenSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
