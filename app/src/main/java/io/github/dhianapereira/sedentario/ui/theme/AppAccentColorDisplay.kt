package io.github.dhianapereira.sedentario.ui.theme

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppAccentColor

@get:StringRes
internal val AppAccentColor.labelRes: Int
    get() = when (this) {
        AppAccentColor.GREEN -> R.string.green
        AppAccentColor.BLUE -> R.string.blue
        AppAccentColor.ORANGE -> R.string.orange
        AppAccentColor.PURPLE -> R.string.purple
    }

@get:StringRes
internal val AppAccentColor.descriptionRes: Int
    get() = when (this) {
        AppAccentColor.GREEN -> R.string.green_description
        AppAccentColor.BLUE -> R.string.blue_description
        AppAccentColor.ORANGE -> R.string.orange_description
        AppAccentColor.PURPLE -> R.string.purple_description
    }

internal val AppAccentColor.previewColor: Color
    get() = when (this) {
        AppAccentColor.GREEN -> Color(0xFF4CAF50)
        AppAccentColor.BLUE -> Color(0xFF1976D2)
        AppAccentColor.ORANGE -> Color(0xFFF57C00)
        AppAccentColor.PURPLE -> Color(0xFF7C3AED)
    }
