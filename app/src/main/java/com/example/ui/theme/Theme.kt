package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val JorezLightColorScheme = lightColorScheme(
    primary = BrandPurple,
    onPrimary = PureWhite,
    primaryContainer = BrandPurpleBg,
    onPrimaryContainer = BrandPurpleDark,
    secondary = LimeGreen,
    onSecondary = PureWhite,
    secondaryContainer = LimeGreenBg,
    onSecondaryContainer = LimeGreenDark,
    tertiary = ElectricBlue,
    onTertiary = PureWhite,
    tertiaryContainer = ElectricBlueBg,
    onTertiaryContainer = ElectricBlueDark,
    background = LightBackground,
    onBackground = TextDark,
    surface = LightSurface,
    onSurface = TextDark,
    surfaceVariant = LightSurfaceSubtle,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderLight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = JorezLightColorScheme,
        typography = Typography,
        content = content
    )
}
