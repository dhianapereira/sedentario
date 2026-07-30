package io.github.dhianapereira.sedentario.ui.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppAccentColor
import io.github.dhianapereira.sedentario.ui.theme.descriptionRes
import io.github.dhianapereira.sedentario.ui.theme.labelRes
import io.github.dhianapereira.sedentario.ui.theme.previewColor

@Composable
fun ColorSelectionSheet(
    selectedColor: AppAccentColor,
    onColorSelected: (AppAccentColor) -> Unit,
    onDismiss: () -> Unit,
) {
    AdaptiveModalBottomSheet(stringResource(R.string.choose_color), onDismiss) {
        AppAccentColor.entries.forEach { color ->
            SettingsSelectionItem(
                label = stringResource(color.labelRes),
                description = stringResource(color.descriptionRes),
                swatchColor = color.previewColor,
                selected = color == selectedColor,
                onClick = { onColorSelected(color) },
            )
        }
    }
}
