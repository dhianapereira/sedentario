package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TrackerCalendar(
    month: YearMonth,
    today: LocalDate,
    entries: Map<LocalDate, WorkoutActivity>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    compactLayout: Boolean,
    modifier: Modifier = Modifier,
) {
    val rowCount = calendarRowCount(month)
    val spacing = if (compactLayout) 6.dp else 8.dp
    val headerSpacing = if (compactLayout) 8.dp else 12.dp

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        val calendarWidth = if (compactLayout) {
            compactCalendarWidth(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                rowCount = rowCount,
                spacing = spacing,
                headerSpacing = headerSpacing,
            )
        } else {
            maxWidth
        }

        Column(modifier = Modifier.width(calendarWidth)) {
            WeekHeader(horizontalSpacing = spacing)
            Spacer(modifier = Modifier.height(headerSpacing))
            TrackerMonthGrid(
                month = month,
                today = today,
                entries = entries,
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                modifier = Modifier.fillMaxWidth(),
                cellSpacing = spacing,
            )
        }
    }
}
