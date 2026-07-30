package io.github.dhianapereira.sedentario.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.AppAccentColor
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.settings.components.ColorSelectionSheet
import io.github.dhianapereira.sedentario.ui.settings.components.LanguageSelectionSheet
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsHeader
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsOptionRow
import io.github.dhianapereira.sedentario.ui.settings.components.SettingsSectionTitle
import io.github.dhianapereira.sedentario.ui.settings.components.ThemeSelectionSheet
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.theme.labelRes

private enum class PreferenceSheet { THEME, COLOR, LANGUAGE }

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onDataBackupClick: () -> Unit,
    onLegalClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    SettingsPage(title = stringResource(R.string.settings), onBackClick = onBackClick) {
        SettingsOptionRow(
            icon = Icons.Outlined.Tune,
            title = stringResource(R.string.preferences),
            value = stringResource(R.string.preferences_page_description),
            onClick = onPreferencesClick,
        )
        SettingsOptionRow(
            icon = Icons.Outlined.Folder,
            title = stringResource(R.string.data_and_backup),
            value = stringResource(R.string.data_backup_page_description),
            onClick = onDataBackupClick,
        )
        SettingsOptionRow(
            icon = Icons.Outlined.Gavel,
            title = stringResource(R.string.legal),
            value = stringResource(R.string.legal_page_description),
            onClick = onLegalClick,
        )
        SettingsOptionRow(
            icon = Icons.Outlined.Info,
            title = stringResource(R.string.about),
            value = stringResource(R.string.about_page_description),
            onClick = onAboutClick,
        )
    }
}

@Composable
fun PreferencesScreen(
    appTheme: AppTheme,
    accentColor: AppAccentColor,
    appLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onColorSelected: (AppAccentColor) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    var openSheet by rememberSaveable { mutableStateOf<PreferenceSheet?>(null) }
    SettingsPage(title = stringResource(R.string.preferences), onBackClick = onBackClick) {
        SettingsSectionTitle(stringResource(R.string.appearance))
        SettingsOptionRow(
            Icons.Outlined.DarkMode,
            stringResource(R.string.theme),
            stringResource(appTheme.labelRes),
        ) { openSheet = PreferenceSheet.THEME }
        SettingsOptionRow(
            Icons.Outlined.Palette,
            stringResource(R.string.accent_color),
            stringResource(accentColor.labelRes),
        ) { openSheet = PreferenceSheet.COLOR }
        Spacer(Modifier.height(24.dp))
        SettingsSectionTitle(stringResource(R.string.preferences))
        SettingsOptionRow(
            Icons.Outlined.Language,
            stringResource(R.string.language),
            stringResource(appLanguage.labelRes),
        ) { openSheet = PreferenceSheet.LANGUAGE }
    }

    when (openSheet) {
        PreferenceSheet.THEME -> ThemeSelectionSheet(appTheme, { openSheet = null; onThemeSelected(it) }) {
            openSheet = null
        }
        PreferenceSheet.COLOR -> ColorSelectionSheet(accentColor, { openSheet = null; onColorSelected(it) }) {
            openSheet = null
        }
        PreferenceSheet.LANGUAGE -> LanguageSelectionSheet(appLanguage, { openSheet = null; onLanguageSelected(it) }) {
            openSheet = null
        }
        null -> Unit
    }
}

@Composable
fun DataBackupScreen(
    onBackClick: () -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
) {
    SettingsPage(title = stringResource(R.string.data_and_backup), onBackClick = onBackClick) {
        SettingsOptionRow(
            Icons.Outlined.FileUpload,
            stringResource(R.string.export_data),
            stringResource(R.string.export_data_description),
            onExportClick,
        )
        SettingsOptionRow(
            Icons.Outlined.FileDownload,
            stringResource(R.string.import_data),
            stringResource(R.string.import_data_description),
            onImportClick,
        )
    }
}

@Composable
fun LegalScreen(
    onBackClick: () -> Unit,
    onPrivacyPolicyClick: (() -> Unit)? = null,
    onTermsOfUseClick: (() -> Unit)? = null,
) {
    SettingsPage(title = stringResource(R.string.legal), onBackClick = onBackClick) {
        SettingsOptionRow(
            Icons.Outlined.PrivacyTip,
            stringResource(R.string.privacy_policy),
            onClick = onPrivacyPolicyClick,
        )
        SettingsOptionRow(
            Icons.Outlined.Description,
            stringResource(R.string.terms_of_use),
            onClick = onTermsOfUseClick,
        )
    }
}

@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val versionName = remember(context) {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    }
    SettingsPage(title = stringResource(R.string.about), onBackClick = onBackClick) {
        Text(
            text = stringResource(R.string.about_app_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp),
        )
        SettingsOptionRow(
            icon = Icons.Outlined.Person,
            title = stringResource(R.string.developed_by),
            value = stringResource(R.string.developer_name),
        )
        SettingsOptionRow(
            icon = Icons.Outlined.Info,
            title = stringResource(R.string.version),
            value = versionName,
        )
    }
}

@Composable
private fun SettingsPage(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
                SettingsHeader(title, onBackClick)
                Spacer(Modifier.height(24.dp))
                content()
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SedentarioTheme {
        SettingsScreen({}, {}, {}, {}, {})
    }
}
