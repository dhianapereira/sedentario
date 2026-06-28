package io.github.dhianapereira.sedentario.ui.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryRepository
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TrackerViewModel internal constructor(
    private val activityEntryRepository: ActivityEntryRepository,
    todayProvider: () -> LocalDate,
) : ViewModel() {
    @Inject constructor(
        activityEntryRepository: ActivityEntryRepository,
    ) : this(activityEntryRepository, LocalDate::now)

    private val _uiState = MutableStateFlow(TrackerUiState(today = todayProvider()))
    val uiState: StateFlow<TrackerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            activityEntryRepository.entries.collect { entries ->
                _uiState.update { it.copy(entries = entries) }
            }
        }
    }

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
        var dateToDelete: LocalDate? = null
        var entryToSave: Pair<LocalDate, WorkoutActivity>? = null
        _uiState.update { state ->
            val selectedDate = state.selectedDate ?: return@update state
            if (state.entries[selectedDate] == activity) {
                dateToDelete = selectedDate
                entryToSave = null
            } else {
                dateToDelete = null
                entryToSave = selectedDate to activity
            }
            state.copy(
                entries = if (entryToSave == null) {
                    state.entries - selectedDate
                } else {
                    state.entries + entryToSave
                },
                isEntrySheetVisible = false,
            )
        }
        viewModelScope.launch {
            dateToDelete?.let { activityEntryRepository.deleteEntry(it) }
            entryToSave?.let { (date, selectedActivity) ->
                activityEntryRepository.saveEntry(date, selectedActivity)
            }
        }
    }
}
