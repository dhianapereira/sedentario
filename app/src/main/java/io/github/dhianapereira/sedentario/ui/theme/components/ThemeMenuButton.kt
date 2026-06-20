package io.github.dhianapereira.sedentario.ui.theme.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.theme.icon
import io.github.dhianapereira.sedentario.ui.theme.label

@Composable
fun ThemeMenuButton(
    appTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(8.dp)
    val primary = MaterialTheme.colorScheme.primary

    Box {
        Row(
            modifier = Modifier
                .clip(shape)
                .border(1.dp, primary, shape)
                .clickable { isMenuOpen = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = appTheme.icon,
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = appTheme.label,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false },
        ) {
            AppTheme.entries.forEach { theme ->
                ThemeMenuItem(
                    theme = theme,
                    selectedTheme = appTheme,
                    onClick = {
                        isMenuOpen = false
                        onThemeSelected(theme)
                    },
                )
            }
        }
    }
}
