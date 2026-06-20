package io.github.dhianapereira.sedentario.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SedentarioColorScheme = darkColorScheme(
    background = Background,
    surface = Surface,
    primary = PurpleLight,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun SedentarioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SedentarioColorScheme,
        content = content,
    )
}
