package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.YearMonth

internal fun calendarRowCount(month: YearMonth): Int {
    val firstDayOffset = month.atDay(1).dayOfWeek.value % 7
    return (firstDayOffset + month.lengthOfMonth() + 6) / 7
}

internal fun compactCalendarWidth(
    maxWidth: Dp,
    maxHeight: Dp,
    rowCount: Int,
    spacing: Dp,
    headerSpacing: Dp,
): Dp {
    val reservedHeight = 24.dp + headerSpacing + spacing * (rowCount - 1)
    val availableCellHeight = (maxHeight - reservedHeight).coerceAtLeast(1.dp)
    val cellSize = (availableCellHeight / rowCount).coerceAtMost(48.dp)
    return minOf(maxWidth, cellSize * 7 + spacing * 6)
}
