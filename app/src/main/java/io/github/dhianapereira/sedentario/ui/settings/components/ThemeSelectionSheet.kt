package io.github.dhianapereira.sedentario.ui.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.theme.labelRes

@Composable
fun ThemeSelectionSheet(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit,
) {
    AdaptiveModalBottomSheet(stringResource(R.string.choose_theme), onDismiss) {
        AppTheme.entries.forEach { theme ->
            SettingsSelectionItem(
                label = stringResource(theme.labelRes),
                selected = theme == selectedTheme,
                onClick = { onThemeSelected(theme) },
            )
        }
    }
}
