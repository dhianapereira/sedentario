package io.github.dhianapereira.sedentario.data.preferences

import io.github.dhianapereira.sedentario.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemePreferencesRepository {
    val appTheme: Flow<AppTheme>

    suspend fun setAppTheme(theme: AppTheme)
}
