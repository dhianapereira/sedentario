package io.github.dhianapereira.sedentario.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.dhianapereira.sedentario.model.AppAccentColor

private data class AccentPalette(
    val darkPrimary: Color,
    val darkContainer: Color,
    val darkOnPrimary: Color,
    val lightPrimary: Color,
    val lightContainer: Color,
    val lightOnPrimary: Color,
    val lightOnContainer: Color,
)

private fun AppAccentColor.palette(): AccentPalette = when (this) {
    AppAccentColor.GREEN -> AccentPalette(
        Color(0xFF66BB6A), Color(0xFF1B5E20), Color(0xFF08210A),
        Color(0xFF2E7D32), Color(0xFFC8E6C9), Color.White, Color(0xFF0D3512),
    )
    AppAccentColor.BLUE -> AccentPalette(
        Color(0xFF64B5F6), Color(0xFF0D47A1), Color(0xFF002033),
        Color(0xFF1976D2), Color(0xFFBBDEFB), Color.White, Color(0xFF062E58),
    )
    AppAccentColor.ORANGE -> AccentPalette(
        Color(0xFFFFB74D), Color(0xFFE65100), Color(0xFF321500),
        Color(0xFFE65100), Color(0xFFFFE0B2), Color.White, Color(0xFF4A1B00),
    )
    AppAccentColor.PURPLE -> AccentPalette(
        DarkPurplePrimary, DarkPurpleContainer, Color.White,
        LightPurplePrimary, LightPurpleContainer, Color.White, Color(0xFF2E1065),
    )
}

private fun createDarkColorScheme(accent: AccentPalette) = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primary = accent.darkPrimary,
    primaryContainer = accent.darkContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFA9A7B3),
    onPrimary = accent.darkOnPrimary,
    onPrimaryContainer = Color.White,
)

private fun createLightColorScheme(accent: AccentPalette) = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    primary = accent.lightPrimary,
    primaryContainer = accent.lightContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    onBackground = Color(0xFF18171C),
    onSurface = Color(0xFF18171C),
    onSurfaceVariant = Color(0xFF5F5B66),
    onPrimary = accent.lightOnPrimary,
    onPrimaryContainer = accent.lightOnContainer,
)

@Composable
fun SedentarioTheme(
    darkTheme: Boolean = true,
    accentColor: AppAccentColor = AppAccentColor.PURPLE,
    content: @Composable () -> Unit,
) {
    val accent = accentColor.palette()
    MaterialTheme(
        colorScheme = if (darkTheme) {
            createDarkColorScheme(accent)
        } else {
            createLightColorScheme(accent)
        },
        content = content,
    )
}
