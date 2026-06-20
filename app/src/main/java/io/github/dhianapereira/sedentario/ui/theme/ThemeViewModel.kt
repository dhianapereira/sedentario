package io.github.dhianapereira.sedentario.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dhianapereira.sedentario.data.preferences.ThemePreferencesRepository
import io.github.dhianapereira.sedentario.model.AppTheme
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferencesRepository: ThemePreferencesRepository,
) : ViewModel() {
    val appTheme: StateFlow<AppTheme> = themePreferencesRepository.appTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AppTheme.DARK,
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferencesRepository.setAppTheme(theme)
        }
    }
}
