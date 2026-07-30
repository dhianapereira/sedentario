package io.github.dhianapereira.sedentario.ui.settings

import io.github.dhianapereira.sedentario.data.backup.BackupException
import io.github.dhianapereira.sedentario.data.backup.BackupDateRange
import io.github.dhianapereira.sedentario.data.backup.BackupImportMode
import io.github.dhianapereira.sedentario.data.backup.BackupRepository
import io.github.dhianapereira.sedentario.ui.theme.MainDispatcherRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `export emits completion with exported count`() = runTest {
        val repository = FakeBackupRepository(exportResult = 12)
        val viewModel = SettingsViewModel(repository)
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.export(TEST_URI, null)
        advanceUntilIdle()

        assertEquals(BackupEvent.Completed(BackupOperation.EXPORT, 12), event.await())
        assertNull(repository.receivedRange)
        assertNull(viewModel.uiState.value.operation)
    }

    @Test
    fun `import emits completion with imported count`() = runTest {
        val repository = FakeBackupRepository(importResult = 7)
        val viewModel = SettingsViewModel(repository)
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.import(TEST_URI, BackupImportMode.MERGE)
        advanceUntilIdle()

        assertEquals(BackupEvent.Completed(BackupOperation.IMPORT, 7), event.await())
        assertEquals(BackupImportMode.MERGE, repository.receivedImportMode)
    }

    @Test
    fun `export forwards selected date range`() = runTest {
        val repository = FakeBackupRepository()
        val viewModel = SettingsViewModel(repository)
        val range = BackupDateRange(10, 20)

        viewModel.export(TEST_URI, range)
        advanceUntilIdle()

        assertEquals(range, repository.receivedRange)
    }

    @Test
    fun `period with entries emits export ready`() = runTest {
        val range = BackupDateRange(10, 20)
        val viewModel = SettingsViewModel(FakeBackupRepository(entryCount = 3))
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.preparePeriodExport(range)
        advanceUntilIdle()

        assertEquals(BackupEvent.ExportReady(range), event.await())
    }

    @Test
    fun `empty period emits no data and does not request export`() = runTest {
        val viewModel = SettingsViewModel(FakeBackupRepository(entryCount = 0))
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.preparePeriodExport(BackupDateRange(10, 20))
        advanceUntilIdle()

        assertEquals(BackupEvent.NoDataInPeriod, event.await())
    }

    @Test
    fun `invalid backup emits specific failure`() = runTest {
        val repository = FakeBackupRepository(importError = BackupException.InvalidFile())
        val viewModel = SettingsViewModel(repository)
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.import(TEST_URI, BackupImportMode.REPLACE)
        advanceUntilIdle()

        assertEquals(BackupEvent.Failed(FailureReason.INVALID_FILE), event.await())
        assertNull(viewModel.uiState.value.operation)
    }

    @Test
    fun `file access error emits specific failure`() = runTest {
        val repository = FakeBackupRepository(exportError = BackupException.CannotOpenFile())
        val viewModel = SettingsViewModel(repository)
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.export(TEST_URI, BackupDateRange(10, 20))
        advanceUntilIdle()

        assertEquals(BackupEvent.Failed(FailureReason.FILE_ACCESS), event.await())
    }

    @Test
    fun `cancel stops operation and emits cancelled event`() = runTest {
        val gate = CompletableDeferred<Unit>()
        val viewModel = SettingsViewModel(FakeBackupRepository(exportGate = gate))
        val event = async { viewModel.events.first() }
        runCurrent()

        viewModel.export(TEST_URI, null)
        runCurrent()
        assertEquals(BackupOperation.EXPORT, viewModel.uiState.value.operation)
        viewModel.cancel()
        advanceUntilIdle()

        assertEquals(BackupEvent.Cancelled, event.await())
        assertNull(viewModel.uiState.value.operation)
    }
}

private class FakeBackupRepository(
    private val exportResult: Int = 0,
    private val importResult: Int = 0,
    private val exportError: BackupException? = null,
    private val importError: BackupException? = null,
    private val exportGate: CompletableDeferred<Unit>? = null,
    private val entryCount: Int = 0,
) : BackupRepository {
    var receivedRange: BackupDateRange? = null
    var receivedImportMode: BackupImportMode? = null

    override suspend fun countEntries(range: BackupDateRange): Int = entryCount

    override suspend fun exportTo(uri: String, range: BackupDateRange?): Int {
        receivedRange = range
        exportGate?.await()
        exportError?.let { throw it }
        return exportResult
    }

    override suspend fun importFrom(uri: String, mode: BackupImportMode): Int {
        receivedImportMode = mode
        importError?.let { throw it }
        return importResult
    }
}

private const val TEST_URI = "content://sedentario.test/backup.json"
