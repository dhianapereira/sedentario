package io.github.dhianapereira.sedentario.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.settings.components.LanguageSelectionSheet
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsHeader
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsOptionRow
import io.github.dhianapereira.sedentario.ui.settings.components.ThemeSelectionSheet
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.theme.labelRes

private enum class SettingsSheet {
    THEME,
    LANGUAGE,
}

@Composable
fun SettingsScreen(
    appTheme: AppTheme,
    appLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    var openSheet by rememberSaveable { mutableStateOf<SettingsSheet?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxSize(),
            ) {
                SettingsHeader(onBackClick = onBackClick)
                Spacer(modifier = Modifier.height(24.dp))
                SettingsOptionRow(
                    icon = Icons.Outlined.Palette,
                    title = stringResource(R.string.theme),
                    value = stringResource(appTheme.labelRes),
                    onClick = { openSheet = SettingsSheet.THEME },
                )
                SettingsOptionRow(
                    icon = Icons.Outlined.Language,
                    title = stringResource(R.string.language),
                    value = stringResource(appLanguage.labelRes),
                    onClick = { openSheet = SettingsSheet.LANGUAGE },
                )
            }
        }
    }

    when (openSheet) {
        SettingsSheet.THEME -> ThemeSelectionSheet(
            selectedTheme = appTheme,
            onThemeSelected = { theme ->
                openSheet = null
                onThemeSelected(theme)
            },
            onDismiss = { openSheet = null },
        )

        SettingsSheet.LANGUAGE -> LanguageSelectionSheet(
            selectedLanguage = appLanguage,
            onLanguageSelected = { language ->
                openSheet = null
                onLanguageSelected(language)
            },
            onDismiss = { openSheet = null },
        )

        null -> Unit
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SedentarioTheme {
        SettingsScreen(
            appTheme = AppTheme.DARK,
            appLanguage = AppLanguage.PORTUGUESE,
            onBackClick = {},
            onThemeSelected = {},
            onLanguageSelected = {},
        )
    }
}
