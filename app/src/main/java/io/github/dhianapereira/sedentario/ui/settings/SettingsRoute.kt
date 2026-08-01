package io.github.dhianapereira.sedentario.ui.settings

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.data.backup.BackupDateRange
import io.github.dhianapereira.sedentario.data.backup.BackupImportMode
import io.github.dhianapereira.sedentario.model.AppAccentColor
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.settings.components.BackupProgressDialog
import io.github.dhianapereira.sedentario.ui.settings.components.ExportDateRangeDialog
import io.github.dhianapereira.sedentario.ui.settings.components.ExportModeSheet
import io.github.dhianapereira.sedentario.ui.settings.components.ImportModeSheet

private enum class SettingsPage { MAIN, PREFERENCES, DATA_BACKUP, LEGAL, ABOUT }

private const val PRIVACY_POLICY_URL =
    "https://dhianapereira.github.io/apps/sedentario/legal/politica-de-privacidade/"
private const val TERMS_OF_USE_URL =
    "https://dhianapereira.github.io/apps/sedentario/legal/termos-de-uso/"

@Composable
fun SettingsRoute(
    appTheme: AppTheme,
    accentColor: AppAccentColor,
    appLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onColorSelected: (AppAccentColor) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val backupState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var page by rememberSaveable { mutableStateOf(SettingsPage.MAIN) }
    var showImportOptions by rememberSaveable { mutableStateOf(false) }
    var showExportOptions by rememberSaveable { mutableStateOf(false) }
    var showDateRangePicker by rememberSaveable { mutableStateOf(false) }
    var pendingImportMode by remember { mutableStateOf<BackupImportMode?>(null) }
    var pendingExportRange by remember { mutableStateOf<BackupDateRange?>(null) }
    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        uri?.toString()?.let { viewModel.export(it, pendingExportRange) }
        pendingExportRange = null
    }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        val mode = pendingImportMode
        if (uri != null && mode != null) viewModel.import(uri.toString(), mode)
        pendingImportMode = null
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is BackupEvent.ExportReady) {
                pendingExportRange = event.range
                exportLauncher.launch("sedentario-backup.json")
            } else {
                snackbarHostState.showSnackbar(event.message(context))
            }
        }
    }

    BackHandler(enabled = page != SettingsPage.MAIN) { page = SettingsPage.MAIN }

    Box {
        when (page) {
            SettingsPage.MAIN -> SettingsScreen(
                onBackClick = onBackClick,
                onPreferencesClick = { page = SettingsPage.PREFERENCES },
                onDataBackupClick = { page = SettingsPage.DATA_BACKUP },
                onLegalClick = { page = SettingsPage.LEGAL },
                onAboutClick = { page = SettingsPage.ABOUT },
            )
            SettingsPage.PREFERENCES -> PreferencesScreen(
                appTheme = appTheme,
                accentColor = accentColor,
                appLanguage = appLanguage,
                onBackClick = { page = SettingsPage.MAIN },
                onThemeSelected = onThemeSelected,
                onColorSelected = onColorSelected,
                onLanguageSelected = onLanguageSelected,
            )
            SettingsPage.DATA_BACKUP -> DataBackupScreen(
                onBackClick = { page = SettingsPage.MAIN },
                onExportClick = { showExportOptions = true },
                onImportClick = { showImportOptions = true },
            )
            SettingsPage.LEGAL -> LegalScreen(
                onBackClick = { page = SettingsPage.MAIN },
                onPrivacyPolicyClick = { uriHandler.openUri(PRIVACY_POLICY_URL) },
                onTermsOfUseClick = { uriHandler.openUri(TERMS_OF_USE_URL) },
            )
            SettingsPage.ABOUT -> AboutScreen(onBackClick = { page = SettingsPage.MAIN })
        }
        SnackbarHost(
            snackbarHostState,
            Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        )
    }

    if (showImportOptions) {
        ImportModeSheet(
            onConfirm = { mode ->
                showImportOptions = false
                pendingImportMode = mode
                importLauncher.launch(arrayOf("application/json", "text/json", "text/plain"))
            },
            onDismiss = { showImportOptions = false },
        )
    }
    if (showExportOptions) {
        ExportModeSheet(
            onExportAll = {
                showExportOptions = false
                pendingExportRange = null
                exportLauncher.launch("sedentario-backup.json")
            },
            onChoosePeriod = {
                showExportOptions = false
                showDateRangePicker = true
            },
            onDismiss = { showExportOptions = false },
        )
    }
    if (showDateRangePicker) {
        ExportDateRangeDialog(
            onConfirm = { range ->
                showDateRangePicker = false
                viewModel.preparePeriodExport(range)
            },
            onDismiss = { showDateRangePicker = false },
        )
    }
    backupState.operation?.let { BackupProgressDialog(it, viewModel::cancel) }
}

private fun BackupEvent.message(context: Context): String = when (this) {
    is BackupEvent.ExportReady -> error("ExportReady does not display a message")
    is BackupEvent.Completed -> context.resources.getQuantityString(
        if (operation == BackupOperation.EXPORT) R.plurals.export_success else R.plurals.import_success,
        entryCount,
        entryCount,
    )
    is BackupEvent.Failed -> context.getString(when (reason) {
        FailureReason.INVALID_FILE -> R.string.invalid_backup
        FailureReason.UNSUPPORTED_VERSION -> R.string.unsupported_backup
        FailureReason.TOO_MANY_ENTRIES -> R.string.backup_too_large
        FailureReason.EMPTY_BACKUP -> R.string.empty_backup_restore_blocked
        FailureReason.FILE_ACCESS -> R.string.file_access_error
        FailureReason.UNKNOWN -> R.string.unknown_backup_error
    })
    BackupEvent.NoDataInPeriod -> context.getString(R.string.no_data_in_period)
    BackupEvent.Cancelled -> context.getString(R.string.operation_cancelled)
}
