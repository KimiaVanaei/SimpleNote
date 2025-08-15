package ir.sharif.simplenote.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = ColorPalette.PrimaryBase,
    onPrimary = ColorPalette.NeutralWhite,
    primaryContainer = ColorPalette.PrimaryBase,
    onPrimaryContainer = ColorPalette.PrimaryDark,

    secondary = ColorPalette.SecondaryBase,
    onSecondary = ColorPalette.NeutralWhite,
    secondaryContainer = ColorPalette.SecondaryLight,
    onSecondaryContainer = ColorPalette.SecondaryDark,

    tertiary = ColorPalette.WarningBase,
    onTertiary = ColorPalette.NeutralBlack,
    tertiaryContainer = ColorPalette.WarningLight,
    onTertiaryContainer = ColorPalette.WarningDark,

    error = ColorPalette.ErrorBase,
    onError = ColorPalette.NeutralWhite,
    errorContainer = ColorPalette.ErrorLight,
    onErrorContainer = ColorPalette.ErrorDark,

    background = ColorPalette.NeutralWhite,
    onBackground = ColorPalette.NeutralBlack,

    surface = ColorPalette.NeutralWhite,
    onSurface = ColorPalette.NeutralBlack,
    surfaceVariant = ColorPalette.NeutralLightGrey,
    onSurfaceVariant = ColorPalette.NeutralDarkGrey,

    outline = ColorPalette.NeutralBaseGrey,
    outlineVariant = ColorPalette.NeutralLightGrey,

    scrim = ColorPalette.NeutralBlack,
    inverseSurface = ColorPalette.NeutralBlack,
    inverseOnSurface = ColorPalette.NeutralWhite,
    inversePrimary = ColorPalette.PrimaryLight,

    surfaceDim = ColorPalette.NeutralLightGrey,
    surfaceBright = ColorPalette.NeutralWhite,
    surfaceContainerLowest = ColorPalette.NeutralWhite,
    surfaceContainerLow = ColorPalette.PrimaryBackground,
    surfaceContainer = ColorPalette.NeutralLightGrey,
    surfaceContainerHigh = ColorPalette.NeutralBaseGrey,
    surfaceContainerHighest = ColorPalette.NeutralDarkGrey,
)

// Dark Theme Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = ColorPalette.PrimaryBase,
    onPrimary = ColorPalette.PrimaryDark,
    primaryContainer = ColorPalette.PrimaryBase,
    onPrimaryContainer = ColorPalette.PrimaryLight,

    secondary = ColorPalette.SecondaryLight,
    onSecondary = ColorPalette.SecondaryDark,
    secondaryContainer = ColorPalette.SecondaryDark,
    onSecondaryContainer = ColorPalette.SecondaryLight,

    tertiary = ColorPalette.WarningLight,
    onTertiary = ColorPalette.WarningDark,
    tertiaryContainer = ColorPalette.WarningDark,
    onTertiaryContainer = ColorPalette.WarningLight,

    error = ColorPalette.ErrorLight,
    onError = ColorPalette.ErrorDark,
    errorContainer = ColorPalette.ErrorDark,
    onErrorContainer = ColorPalette.ErrorLight,

    background = ColorPalette.NeutralBlack,
    onBackground = ColorPalette.NeutralWhite,

    surface = ColorPalette.NeutralBlack,
    onSurface = ColorPalette.NeutralWhite,
    surfaceVariant = ColorPalette.NeutralDarkGrey,
    onSurfaceVariant = ColorPalette.NeutralLightGrey,

    outline = ColorPalette.NeutralBaseGrey,
    outlineVariant = ColorPalette.NeutralDarkGrey,

    scrim = ColorPalette.NeutralBlack,
    inverseSurface = ColorPalette.NeutralWhite,
    inverseOnSurface = ColorPalette.NeutralBlack,
    inversePrimary = ColorPalette.PrimaryBase,

    surfaceDim = ColorPalette.NeutralBlack,
    surfaceBright = ColorPalette.NeutralDarkGrey,
    surfaceContainerLowest = ColorPalette.NeutralBlack,
    surfaceContainerLow = ColorPalette.NeutralBlack,
    surfaceContainer = ColorPalette.NeutralDarkGrey,
    surfaceContainerHigh = ColorPalette.NeutralDarkGrey,
    surfaceContainerHighest = ColorPalette.NeutralBaseGrey,
)

@Composable
fun SimpleNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}