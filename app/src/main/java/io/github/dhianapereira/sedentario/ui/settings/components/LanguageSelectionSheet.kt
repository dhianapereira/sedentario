package io.github.dhianapereira.sedentario.ui.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.ui.settings.labelRes

@Composable
fun LanguageSelectionSheet(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit,
) {
    AdaptiveModalBottomSheet(stringResource(R.string.choose_language), onDismiss) {
        AppLanguage.entries.forEach { language ->
            SettingsSelectionItem(
                label = stringResource(language.labelRes),
                selected = language == selectedLanguage,
                onClick = { onLanguageSelected(language) },
            )
        }
    }
}
