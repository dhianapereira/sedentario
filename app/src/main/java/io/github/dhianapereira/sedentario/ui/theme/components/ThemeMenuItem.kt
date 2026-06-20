package io.github.dhianapereira.sedentario.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.theme.icon
import io.github.dhianapereira.sedentario.ui.theme.label

@Composable
internal fun ThemeMenuItem(
    theme: AppTheme,
    selectedTheme: AppTheme,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(theme.label) },
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = theme.icon,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (theme == selectedTheme) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                )
            }
        },
    )
}
