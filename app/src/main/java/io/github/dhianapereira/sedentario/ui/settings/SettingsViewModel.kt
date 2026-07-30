package io.github.dhianapereira.sedentario.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dhianapereira.sedentario.data.backup.BackupException
import io.github.dhianapereira.sedentario.data.backup.BackupDateRange
import io.github.dhianapereira.sedentario.data.backup.BackupImportMode
import io.github.dhianapereira.sedentario.data.backup.BackupRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class BackupOperation { EXPORT, IMPORT }

data class SettingsBackupUiState(val operation: BackupOperation? = null)

sealed interface BackupEvent {
    data class ExportReady(val range: BackupDateRange) : BackupEvent
    data class Completed(val operation: BackupOperation, val entryCount: Int) : BackupEvent
    data class Failed(val reason: FailureReason) : BackupEvent
    data object NoDataInPeriod : BackupEvent
    data object Cancelled : BackupEvent
}

enum class FailureReason { INVALID_FILE, UNSUPPORTED_VERSION, TOO_MANY_ENTRIES, EMPTY_BACKUP, FILE_ACCESS, UNKNOWN }

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupRepository: BackupRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsBackupUiState())
    val uiState: StateFlow<SettingsBackupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BackupEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<BackupEvent> = _events.asSharedFlow()
    private var backupJob: Job? = null

    fun export(uri: String, range: BackupDateRange?) =
        run(BackupOperation.EXPORT) { backupRepository.exportTo(uri, range) }

    fun preparePeriodExport(range: BackupDateRange) {
        if (backupJob?.isActive == true) return
        backupJob = viewModelScope.launch {
            try {
                if (backupRepository.countEntries(range) == 0) {
                    _events.emit(BackupEvent.NoDataInPeriod)
                } else {
                    _events.emit(BackupEvent.ExportReady(range))
                }
            } catch (_: Exception) {
                _events.emit(BackupEvent.Failed(FailureReason.UNKNOWN))
            }
        }
    }

    fun import(uri: String, mode: BackupImportMode) =
        run(BackupOperation.IMPORT) { backupRepository.importFrom(uri, mode) }

    fun cancel() {
        backupJob?.cancel()
    }

    private fun run(operation: BackupOperation, block: suspend () -> Int) {
        if (backupJob?.isActive == true) return
        backupJob = viewModelScope.launch {
            _uiState.value = SettingsBackupUiState(operation)
            try {
                _events.emit(BackupEvent.Completed(operation, block()))
            } catch (_: CancellationException) {
                _events.emit(BackupEvent.Cancelled)
            } catch (exception: BackupException) {
                _events.emit(BackupEvent.Failed(exception.toFailureReason()))
            } catch (_: Exception) {
                _events.emit(BackupEvent.Failed(FailureReason.UNKNOWN))
            } finally {
                _uiState.value = SettingsBackupUiState()
            }
        }
    }

    private fun BackupException.toFailureReason() = when (this) {
        is BackupException.InvalidFile -> FailureReason.INVALID_FILE
        is BackupException.UnsupportedVersion -> FailureReason.UNSUPPORTED_VERSION
        is BackupException.TooManyEntries -> FailureReason.TOO_MANY_ENTRIES
        is BackupException.CannotOpenFile -> FailureReason.FILE_ACCESS
        is BackupException.EmptyBackup -> FailureReason.EMPTY_BACKUP
    }
}
