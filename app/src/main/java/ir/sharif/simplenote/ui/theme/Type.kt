package ir.sharif.simplenote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


object TextStyles {

    // Text XS - 10px
    val textXs = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 10.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.sp,
    )

    val textXsMedium = textXs.copy(fontWeight = FontWeight.Medium) // 500
    val textXsBold = textXs.copy(fontWeight = FontWeight.Bold) // 700

    // Text 2XS - 12px
    val text2Xs = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.sp,
    )

    val text2XsMedium = text2Xs.copy(fontWeight = FontWeight.Medium) // 500
    val text2XsBold = text2Xs.copy(fontWeight = FontWeight.Bold) // 700

    // Text SM - 14px
    val textSm = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.sp,
    )

    val textSmMedium = textSm.copy(fontWeight = FontWeight.Medium) // 500
    val textSmBold = textSm.copy(fontWeight = FontWeight.Bold) // 700

    // Text Base - 16px
    val textBase = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 16.sp,
        lineHeight = 22.4.sp,
        letterSpacing = 0.sp,
    )

    val textBaseMedium = textBase.copy(fontWeight = FontWeight.Medium) // 500
    val textBaseBold = textBase.copy(fontWeight = FontWeight.Bold) // 700

    // Text LG - 20px
    val textLg = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    )

    val textLgMedium = textLg.copy(fontWeight = FontWeight.Medium) // 500
    val textLgBold = textLg.copy(fontWeight = FontWeight.Bold) // 700

    // Text XL - 24px
    val textXl = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 24.sp,
        lineHeight = 28.8.sp,
        letterSpacing = 0.sp,
    )

    val textXlMedium = textXl.copy(fontWeight = FontWeight.Medium) // 500
    val textXlBold = textXl.copy(fontWeight = FontWeight.Bold) // 700

    // Text 2XL - 32px
    val text2Xl = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 32.sp,
        lineHeight = 38.4.sp,
        letterSpacing = 0.sp,
    )

    val text2XlMedium = text2Xl.copy(fontWeight = FontWeight.Medium) // 500
    val text2XlBold = text2Xl.copy(fontWeight = FontWeight.Bold) // 700

    // Text 3XL - 40px
    val text3Xl = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 40.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    )

    val text3XlMedium = text3Xl.copy(fontWeight = FontWeight.Medium) // 500
    val text3XlBold = text3Xl.copy(fontWeight = FontWeight.Bold) // 700
}

val Typography = Typography(
    displayLarge = TextStyles.text3Xl,
    displayMedium = TextStyles.text2Xl,
    displaySmall = TextStyles.textXl,

    headlineLarge = TextStyles.textXl,
    headlineMedium = TextStyles.textLg,
    headlineSmall = TextStyles.textBase,

    titleLarge = TextStyles.textLgMedium,
    titleMedium = TextStyles.textBaseMedium,
    titleSmall = TextStyles.textSmMedium,

    bodyLarge = TextStyles.textBase,
    bodyMedium = TextStyles.textSm,
    bodySmall = TextStyles.text2Xs,

    labelLarge = TextStyles.textSmMedium,
    labelMedium = TextStyles.text2XsMedium,
    labelSmall = TextStyles.textXsMedium
)