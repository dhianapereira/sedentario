package io.github.dhianapereira.sedentario.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.dhianapereira.sedentario.model.AppTheme
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.userPreferencesDataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

class DataStoreThemePreferencesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : ThemePreferencesRepository {
    override val appTheme: Flow<AppTheme> = context.userPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences -> preferences.readAppTheme() }

    override suspend fun setAppTheme(theme: AppTheme) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.APP_THEME] = theme.name
        }
    }

    private fun Preferences.readAppTheme(): AppTheme {
        val savedTheme = this[Keys.APP_THEME]
        return AppTheme.entries.firstOrNull { it.name == savedTheme } ?: AppTheme.DARK
    }

    private object Keys {
        val APP_THEME = stringPreferencesKey("app_theme")
    }
}
