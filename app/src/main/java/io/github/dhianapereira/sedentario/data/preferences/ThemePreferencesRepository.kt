package io.github.dhianapereira.sedentario.data.preferences

import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.model.AppAccentColor
import kotlinx.coroutines.flow.Flow

interface ThemePreferencesRepository {
    val appTheme: Flow<AppTheme>
    val accentColor: Flow<AppAccentColor>

    suspend fun setAppTheme(theme: AppTheme)
    suspend fun setAccentColor(color: AppAccentColor)
}
