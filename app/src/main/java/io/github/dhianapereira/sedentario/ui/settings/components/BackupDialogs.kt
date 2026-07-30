package io.github.dhianapereira.sedentario.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.data.backup.BackupDateRange
import io.github.dhianapereira.sedentario.data.backup.BackupImportMode
import io.github.dhianapereira.sedentario.ui.settings.BackupOperation

@Composable
fun ImportModeSheet(
    onConfirm: (BackupImportMode) -> Unit,
    onDismiss: () -> Unit,
) {
    var selection by rememberSaveable { mutableStateOf(BackupImportMode.MERGE) }
    AdaptiveModalBottomSheet(stringResource(R.string.choose_import_mode), onDismiss) {
        Column {
                ChoiceRow(
                    selected = selection == BackupImportMode.MERGE,
                    title = stringResource(R.string.merge_data),
                    description = stringResource(R.string.merge_data_description),
                    onClick = { selection = BackupImportMode.MERGE },
                )
                ChoiceRow(
                    selected = selection == BackupImportMode.REPLACE,
                    title = stringResource(R.string.restore_backup),
                    description = stringResource(R.string.restore_backup_description),
                    onClick = { selection = BackupImportMode.REPLACE },
                )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onConfirm(selection) }) { Text(stringResource(R.string.continue_action)) }
        }
    }
}

@Composable
fun ExportModeSheet(
    onExportAll: () -> Unit,
    onChoosePeriod: () -> Unit,
    onDismiss: () -> Unit,
) {
    AdaptiveModalBottomSheet(stringResource(R.string.choose_export_scope), onDismiss) {
        Column {
                ChoiceRow(
                    selected = false,
                    title = stringResource(R.string.export_all),
                    description = stringResource(R.string.export_all_description),
                    onClick = onExportAll,
                    showRadio = false,
                )
                ChoiceRow(
                    selected = false,
                    title = stringResource(R.string.export_period),
                    description = stringResource(R.string.export_period_description),
                    onClick = onChoosePeriod,
                    showRadio = false,
                )
        }
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.End),
        ) { Text(stringResource(R.string.cancel)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportDateRangeDialog(
    onConfirm: (BackupDateRange) -> Unit,
    onDismiss: () -> Unit,
) {
    val state = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {
            Button(
                enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null,
                onClick = {
                    onConfirm(
                        BackupDateRange(
                            startEpochDay = Math.floorDiv(state.selectedStartDateMillis!!, MILLIS_PER_DAY),
                            endEpochDay = Math.floorDiv(state.selectedEndDateMillis!!, MILLIS_PER_DAY),
                        ),
                    )
                },
            ) { Text(stringResource(R.string.export_data)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text(
                    text = stringResource(R.string.select_export_period),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                )
            },
            showModeToggle = false,
        )
    }
}

@Composable
private fun ChoiceRow(
    selected: Boolean,
    title: String,
    description: String,
    onClick: () -> Unit,
    showRadio: Boolean = true,
) {
    val shape = RoundedCornerShape(14.dp)
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(if (selected) 2.dp else 1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showRadio) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(Modifier.width(8.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title)
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!showRadio) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
fun BackupProgressDialog(operation: BackupOperation, onCancel: () -> Unit) {
    AdaptiveModalBottomSheet(
        title = stringResource(if (operation == BackupOperation.EXPORT) R.string.exporting else R.string.importing),
        onDismiss = {},
        dismissAllowed = false,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator()
            Spacer(Modifier.width(16.dp))
            Text(stringResource(R.string.keep_app_open))
        }
        TextButton(
            onClick = onCancel,
            modifier = Modifier.align(Alignment.End),
        ) { Text(stringResource(R.string.cancel)) }
    }
}

private const val MILLIS_PER_DAY = 86_400_000L
