package io.github.dhianapereira.sedentario.ui.theme

import androidx.annotation.StringRes
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppTheme

@get:StringRes
internal val AppTheme.labelRes: Int
    get() = when (this) {
        AppTheme.SYSTEM -> R.string.system_theme
        AppTheme.DARK -> R.string.dark_theme
        AppTheme.LIGHT -> R.string.light_theme
    }
