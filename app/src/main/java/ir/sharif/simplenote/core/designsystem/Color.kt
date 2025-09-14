package ir.sharif.simplenote.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * System-adaptive palette:
 * - These are @Composable getters that read from MaterialTheme.colorScheme.
 * - Keep using the same ColorPalette.* names across the app; they will follow system theme.
 */
object ColorPalette {
    // ---- Neutral (map to scheme neutrals)
    val NeutralBlack: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface
    val NeutralDarkGrey: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    val NeutralBaseGrey: Color
        @Composable get() = MaterialTheme.colorScheme.outlineVariant
    val NeutralLightGrey: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val NeutralWhite: Color
        @Composable get() = MaterialTheme.colorScheme.surface

    // ---- Error
    val ErrorBase: Color
        @Composable get() = MaterialTheme.colorScheme.error
    val ErrorDark: Color
        @Composable get() = MaterialTheme.colorScheme.onError
    val ErrorLight: Color
        @Composable get() = MaterialTheme.colorScheme.errorContainer

    // ---- Primary
    val PrimaryBase: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    val PrimaryDark: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary
    val PrimaryLight: Color
        @Composable get() = MaterialTheme.colorScheme.primaryContainer
    val PrimaryBackground: Color
        @Composable get() = MaterialTheme.colorScheme.background

    // ---- Success
    val SuccessBase: Color
        @Composable get() = MaterialTheme.colorScheme.tertiary
    val SuccessDark: Color
        @Composable get() = MaterialTheme.colorScheme.onTertiary
    val SuccessLight: Color
        @Composable get() = MaterialTheme.colorScheme.tertiaryContainer

    // ---- Warning
    val WarningBase: Color
        @Composable get() = MaterialTheme.colorScheme.secondary
    val WarningDark: Color
        @Composable get() = MaterialTheme.colorScheme.onSecondary
    val WarningLight: Color
        @Composable get() = MaterialTheme.colorScheme.secondaryContainer

    // ---- Secondary (kept for backward compatibility)
    val SecondaryBase: Color
        @Composable get() = MaterialTheme.colorScheme.secondary
    val SecondaryDark: Color
        @Composable get() = MaterialTheme.colorScheme.onSecondary
    val SecondaryLight: Color
        @Composable get() = MaterialTheme.colorScheme.secondaryContainer
}
