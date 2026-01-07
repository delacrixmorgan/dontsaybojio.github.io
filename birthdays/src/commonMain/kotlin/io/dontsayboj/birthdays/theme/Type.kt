package io.dontsayboj.birthdays.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dontsaybojio.birthdays.generated.resources.Res
import dontsaybojio.birthdays.generated.resources.lato_regular
import dontsaybojio.birthdays.generated.resources.noto_color_emoji_regular
import org.jetbrains.compose.resources.Font

val latoRegularFontFamily: FontFamily
    @Composable get() = FontFamily(Font(Res.font.lato_regular))

val notoColorEmojiFontFamily: FontFamily
    @Composable get() = FontFamily(Font(Res.font.noto_color_emoji_regular, weight = FontWeight.Normal))

val AppTypography: Typography
    @Composable get() {
        val baseline = Typography()
        return Typography(
            displayLarge = baseline.displayLarge.copy(fontFamily = latoRegularFontFamily),
            displayMedium = baseline.displayMedium.copy(fontFamily = latoRegularFontFamily),
            displaySmall = baseline.displaySmall.copy(fontFamily = latoRegularFontFamily),
            headlineLarge = baseline.headlineLarge.copy(fontFamily = latoRegularFontFamily),
            headlineMedium = baseline.headlineMedium.copy(fontFamily = latoRegularFontFamily),
            headlineSmall = baseline.headlineSmall.copy(fontFamily = latoRegularFontFamily),
            titleLarge = baseline.titleLarge.copy(fontFamily = latoRegularFontFamily),
            titleMedium = baseline.titleMedium.copy(fontFamily = latoRegularFontFamily),
            titleSmall = baseline.titleSmall.copy(fontFamily = latoRegularFontFamily),
            bodyLarge = baseline.bodyLarge.copy(fontFamily = latoRegularFontFamily),
            bodyMedium = baseline.bodyMedium.copy(fontFamily = latoRegularFontFamily),
            bodySmall = baseline.bodySmall.copy(fontFamily = latoRegularFontFamily),
            labelLarge = baseline.labelLarge.copy(fontFamily = latoRegularFontFamily),
            labelMedium = baseline.labelMedium.copy(fontFamily = latoRegularFontFamily),
            labelSmall = baseline.labelSmall.copy(fontFamily = latoRegularFontFamily),
        )
    }