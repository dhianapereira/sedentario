package io.github.dhianapereira.sedentario.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.dhianapereira.sedentario.model.AppTheme

internal val AppTheme.label: String
    get() = when (this) {
        AppTheme.SYSTEM -> "Sistema"
        AppTheme.DARK -> "Escuro"
        AppTheme.LIGHT -> "Claro"
    }

internal val AppTheme.icon: ImageVector
    get() = when (this) {
        AppTheme.SYSTEM -> Icons.Outlined.SettingsBrightness
        AppTheme.DARK -> Icons.Outlined.DarkMode
        AppTheme.LIGHT -> Icons.Outlined.LightMode
    }
