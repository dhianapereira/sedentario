package io.github.dhianapereira.sedentario.ui.tracker

import io.github.dhianapereira.sedentario.model.WorkoutActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class TrackerViewModelTest {
    @Test
    fun `future dates cannot be selected`() {
        val viewModel = TrackerViewModel()
        val initialState = viewModel.uiState.value

        viewModel.selectDate(initialState.today.plusDays(1))

        assertEquals(initialState.today, viewModel.uiState.value.selectedDate)
    }

    @Test
    fun `activity is stored for selected date and sheet is closed`() {
        val viewModel = TrackerViewModel()
        val selectedDate = viewModel.uiState.value.today.minusDays(1)

        viewModel.selectDate(selectedDate)
        viewModel.showEntrySheet()
        viewModel.selectActivity(WorkoutActivity.RUN)

        val state = viewModel.uiState.value
        assertEquals(WorkoutActivity.RUN, state.entries[selectedDate])
        assertFalse(state.isEntrySheetVisible)
    }
}
