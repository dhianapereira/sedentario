package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TrackerMonthGrid(
    month: YearMonth,
    today: LocalDate,
    entries: Map<LocalDate, WorkoutActivity>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    cellSpacing: Dp = 8.dp,
) {
    val firstDayOffset = month.atDay(1).dayOfWeek.value % 7
    val cells = firstDayOffset + month.lengthOfMonth()
    val rows = (cells + 6) / 7

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(cellSpacing),
    ) {
        repeat(rows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(cellSpacing),
            ) {
                repeat(7) { column ->
                    val day = row * 7 + column - firstDayOffset + 1
                    if (day in 1..month.lengthOfMonth()) {
                        val date = month.atDay(day)
                        DayCell(
                            activity = entries[date],
                            isToday = date == today,
                            isSelected = date == selectedDate,
                            isLocked = date.isAfter(today),
                            onClick = { onDateSelected(date) },
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                        )
                    }
                }
            }
        }
    }
}
