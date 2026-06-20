package io.github.dhianapereira.sedentario.ui.settings

import androidx.annotation.StringRes
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppLanguage

@get:StringRes
internal val AppLanguage.labelRes: Int
    get() = when (this) {
        AppLanguage.PORTUGUESE -> R.string.portuguese
        AppLanguage.ENGLISH -> R.string.english
    }
