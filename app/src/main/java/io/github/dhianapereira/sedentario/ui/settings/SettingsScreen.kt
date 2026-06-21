package io.github.dhianapereira.sedentario.ui.settings

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppAccentColor
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.settings.components.LanguageSelectionSheet
import io.github.dhianapereira.sedentario.ui.settings.components.ColorSelectionSheet
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsHeader
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsOptionRow
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsSectionTitle
import io.github.dhianapereira.sedentario.ui.settings.components.ThemeSelectionSheet
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.theme.labelRes

private enum class SettingsSheet {
    THEME,
    COLOR,
    LANGUAGE,
}

@Composable
fun SettingsScreen(
    appTheme: AppTheme,
    accentColor: AppAccentColor,
    appLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onColorSelected: (AppAccentColor) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onPrivacyPolicyClick: (() -> Unit)? = null,
    onTermsOfUseClick: (() -> Unit)? = null,
) {
    var openSheet by rememberSaveable { mutableStateOf<SettingsSheet?>(null) }
    val context = LocalContext.current
    val versionName = remember(context) {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
            .orEmpty()
    }

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
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                SettingsHeader(onBackClick = onBackClick)
                Spacer(modifier = Modifier.height(24.dp))
                SettingsSectionTitle(title = stringResource(R.string.appearance))
                SettingsOptionRow(
                    icon = Icons.Outlined.DarkMode,
                    title = stringResource(R.string.theme),
                    value = stringResource(appTheme.labelRes),
                    onClick = { openSheet = SettingsSheet.THEME },
                )
                SettingsOptionRow(
                    icon = Icons.Outlined.Palette,
                    title = stringResource(R.string.accent_color),
                    value = stringResource(accentColor.labelRes),
                    onClick = { openSheet = SettingsSheet.COLOR },
                )
                Spacer(modifier = Modifier.height(24.dp))
                SettingsSectionTitle(title = stringResource(R.string.preferences))
                SettingsOptionRow(
                    icon = Icons.Outlined.Language,
                    title = stringResource(R.string.language),
                    value = stringResource(appLanguage.labelRes),
                    onClick = { openSheet = SettingsSheet.LANGUAGE },
                )
                Spacer(modifier = Modifier.height(24.dp))
                SettingsSectionTitle(title = stringResource(R.string.legal))
                SettingsOptionRow(
                    icon = Icons.Outlined.PrivacyTip,
                    title = stringResource(R.string.privacy_policy),
                    onClick = onPrivacyPolicyClick,
                )
                SettingsOptionRow(
                    icon = Icons.Outlined.Description,
                    title = stringResource(R.string.terms_of_use),
                    onClick = onTermsOfUseClick,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.app_version, versionName),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
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

        SettingsSheet.COLOR -> ColorSelectionSheet(
            selectedColor = accentColor,
            onColorSelected = { color ->
                openSheet = null
                onColorSelected(color)
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
            accentColor = AppAccentColor.PURPLE,
            appLanguage = AppLanguage.PORTUGUESE,
            onBackClick = {},
            onThemeSelected = {},
            onColorSelected = {},
            onLanguageSelected = {},
        )
    }
}
