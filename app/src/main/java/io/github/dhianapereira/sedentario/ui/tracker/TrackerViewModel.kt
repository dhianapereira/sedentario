package io.github.dhianapereira.sedentario.ui.tracker

import androidx.lifecycle.ViewModel
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TrackerViewModel internal constructor(
    todayProvider: () -> LocalDate,
) : ViewModel() {
    constructor() : this(LocalDate::now)

    private val _uiState = MutableStateFlow(TrackerUiState(today = todayProvider()))
    val uiState: StateFlow<TrackerUiState> = _uiState.asStateFlow()

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            if (date.isAfter(state.today)) state else state.copy(selectedDate = date)
        }
    }

    fun showPreviousMonth() {
        _uiState.update { state ->
            state.copy(
                displayedMonth = state.displayedMonth.minusMonths(1),
                selectedDate = null,
                isEntrySheetVisible = false,
            )
        }
    }

    fun showNextMonth() {
        _uiState.update { state ->
            state.copy(
                displayedMonth = state.displayedMonth.plusMonths(1),
                selectedDate = null,
                isEntrySheetVisible = false,
            )
        }
    }

    fun showEntrySheet() {
        _uiState.update { state ->
            if (!state.canAddEntry) {
                state
            } else {
                val defaultDate = if (state.displayedMonth == YearMonth.from(state.today)) {
                    state.today
                } else {
                    state.displayedMonth.atEndOfMonth()
                }
                state.copy(
                    selectedDate = state.selectedDate ?: defaultDate,
                    isEntrySheetVisible = true,
                )
            }
        }
    }

    fun hideEntrySheet() {
        _uiState.update { it.copy(isEntrySheetVisible = false) }
    }

    fun selectActivity(activity: WorkoutActivity) {
        _uiState.update { state ->
            val selectedDate = state.selectedDate ?: return@update state
            state.copy(
                entries = state.entries + (selectedDate to activity),
                isEntrySheetVisible = false,
            )
        }
    }
}
