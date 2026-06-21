package io.github.dhianapereira.sedentario.ui.tracker

import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth

data class TrackerUiState(
    val today: LocalDate,
    val selectedDate: LocalDate? = today,
    val displayedMonth: YearMonth = YearMonth.from(today),
    val entries: Map<LocalDate, WorkoutActivity> = emptyMap(),
    val isEntrySheetVisible: Boolean = false,
    val availableActivities: List<WorkoutActivity> = WorkoutActivity.entries,
) {
    val canAddEntry: Boolean
        get() = !displayedMonth.isAfter(YearMonth.from(today))
}
