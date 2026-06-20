package io.github.dhianapereira.sedentario.ui.tracker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class TrackerViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(TrackerUiState(today = LocalDate.now()))
    val uiState: StateFlow<TrackerUiState> = _uiState.asStateFlow()

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            if (date.isAfter(state.today)) state else state.copy(selectedDate = date)
        }
    }

    fun showEntrySheet() {
        _uiState.update { it.copy(isEntrySheetVisible = true) }
    }

    fun hideEntrySheet() {
        _uiState.update { it.copy(isEntrySheetVisible = false) }
    }

    fun selectActivity(activity: WorkoutActivity) {
        _uiState.update { state ->
            state.copy(
                entries = state.entries + (state.selectedDate to activity),
                isEntrySheetVisible = false,
            )
        }
    }
}
