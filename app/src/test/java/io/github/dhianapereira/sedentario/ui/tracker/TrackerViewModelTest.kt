package io.github.dhianapereira.sedentario.ui.tracker

import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TrackerViewModelTest {
    private val today = LocalDate.of(2026, 6, 20)

    @Test
    fun `initial state uses current date and month`() {
        val state = createViewModel().uiState.value

        assertEquals(today, state.today)
        assertEquals(today, state.selectedDate)
        assertEquals(YearMonth.of(2026, 6), state.displayedMonth)
    }

    @Test
    fun `future dates cannot be selected`() {
        val viewModel = createViewModel()

        viewModel.selectDate(today.plusDays(1))

        assertEquals(today, viewModel.uiState.value.selectedDate)
    }

    @Test
    fun `past dates can be selected`() {
        val viewModel = createViewModel()
        val pastDate = today.minusDays(4)

        viewModel.selectDate(pastDate)

        assertEquals(pastDate, viewModel.uiState.value.selectedDate)
    }

    @Test
    fun `calendar can navigate to previous and next months`() {
        val viewModel = createViewModel()

        viewModel.showPreviousMonth()
        assertEquals(YearMonth.of(2026, 5), viewModel.uiState.value.displayedMonth)

        viewModel.showNextMonth()
        viewModel.showNextMonth()
        assertEquals(YearMonth.of(2026, 7), viewModel.uiState.value.displayedMonth)
    }

    @Test
    fun `navigating months clears the selected date`() {
        val viewModel = createViewModel()
        val selectedDate = today.minusDays(3)

        viewModel.selectDate(selectedDate)
        viewModel.showNextMonth()

        assertEquals(null, viewModel.uiState.value.selectedDate)
    }

    @Test
    fun `entry sheet does not open in a future month`() {
        val viewModel = createViewModel()

        viewModel.showNextMonth()
        viewModel.showEntrySheet()

        assertFalse(viewModel.uiState.value.isEntrySheetVisible)
    }

    @Test
    fun `new entry defaults to the latest day of a past month`() {
        val viewModel = createViewModel()

        viewModel.showPreviousMonth()
        viewModel.showEntrySheet()

        val state = viewModel.uiState.value
        assertEquals(LocalDate.of(2026, 5, 31), state.selectedDate)
        assertTrue(state.isEntrySheetVisible)
    }

    @Test
    fun `activity is not stored without a selected date`() {
        val viewModel = createViewModel()

        viewModel.showNextMonth()
        viewModel.selectActivity(WorkoutActivity.RUN)

        assertTrue(viewModel.uiState.value.entries.isEmpty())
    }

    @Test
    fun `entry sheet visibility can be changed`() {
        val viewModel = createViewModel()

        viewModel.showEntrySheet()
        assertTrue(viewModel.uiState.value.isEntrySheetVisible)

        viewModel.hideEntrySheet()
        assertFalse(viewModel.uiState.value.isEntrySheetVisible)
    }

    @Test
    fun `activity is stored for selected date and sheet is closed`() {
        val viewModel = createViewModel()
        val selectedDate = today.minusDays(1)

        viewModel.selectDate(selectedDate)
        viewModel.showEntrySheet()
        viewModel.selectActivity(WorkoutActivity.RUN)

        val state = viewModel.uiState.value
        assertEquals(WorkoutActivity.RUN, state.entries[selectedDate])
        assertFalse(state.isEntrySheetVisible)
    }

    @Test
    fun `selecting the current activity again removes it`() {
        val viewModel = createViewModel()

        viewModel.selectActivity(WorkoutActivity.RUN)
        viewModel.showEntrySheet()
        viewModel.selectActivity(WorkoutActivity.RUN)

        val state = viewModel.uiState.value
        assertFalse(state.entries.containsKey(today))
        assertFalse(state.isEntrySheetVisible)
    }

    private fun createViewModel(): TrackerViewModel {
        return TrackerViewModel(todayProvider = { today })
    }
}
