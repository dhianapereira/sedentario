package io.github.dhianapereira.sedentario.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFA9A7B3),
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
)

private val LightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    primary = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    onBackground = Color(0xFF18171C),
    onSurface = Color(0xFF18171C),
    onSurfaceVariant = Color(0xFF5F5B66),
    onPrimary = Color.White,
    onPrimaryContainer = Color(0xFF2E1065),
)

@Composable
fun SedentarioTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}
