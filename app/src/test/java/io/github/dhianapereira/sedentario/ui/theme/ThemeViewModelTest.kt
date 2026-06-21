package io.github.dhianapereira.sedentario.ui.theme

import io.github.dhianapereira.sedentario.data.preferences.ThemePreferencesRepository
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.model.AppAccentColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `saved theme is exposed in state`() = runTest {
        val repository = FakeThemePreferencesRepository(AppTheme.SYSTEM)
        val viewModel = ThemeViewModel(repository)

        advanceUntilIdle()

        assertEquals(AppTheme.SYSTEM, viewModel.appTheme.value)
    }

    @Test
    fun `selecting light theme persists preference`() = runTest {
        val repository = FakeThemePreferencesRepository(AppTheme.DARK)
        val viewModel = ThemeViewModel(repository)
        advanceUntilIdle()

        viewModel.setTheme(AppTheme.LIGHT)
        advanceUntilIdle()

        assertEquals(AppTheme.LIGHT, repository.savedTheme.value)
    }

    @Test
    fun `selecting an accent color persists preference`() = runTest {
        val repository = FakeThemePreferencesRepository(AppTheme.DARK)
        val viewModel = ThemeViewModel(repository)
        advanceUntilIdle()

        viewModel.setAccentColor(AppAccentColor.GREEN)
        advanceUntilIdle()

        assertEquals(AppAccentColor.GREEN, repository.savedAccentColor.value)
    }
}

private class FakeThemePreferencesRepository(initialTheme: AppTheme) : ThemePreferencesRepository {
    val savedTheme = MutableStateFlow(initialTheme)
    override val appTheme = savedTheme.asStateFlow()
    val savedAccentColor = MutableStateFlow(AppAccentColor.PURPLE)
    override val accentColor = savedAccentColor.asStateFlow()

    override suspend fun setAppTheme(theme: AppTheme) {
        savedTheme.value = theme
    }

    override suspend fun setAccentColor(color: AppAccentColor) {
        savedAccentColor.value = color
    }
}
