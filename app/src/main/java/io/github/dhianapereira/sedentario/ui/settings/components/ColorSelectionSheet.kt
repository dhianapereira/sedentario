package io.github.dhianapereira.sedentario.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppAccentColor
import io.github.dhianapereira.sedentario.ui.theme.descriptionRes
import io.github.dhianapereira.sedentario.ui.theme.labelRes
import io.github.dhianapereira.sedentario.ui.theme.previewColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSelectionSheet(
    selectedColor: AppAccentColor,
    onColorSelected: (AppAccentColor) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            Text(
                text = stringResource(R.string.choose_color),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
            )
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
}
