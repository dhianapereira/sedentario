package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.ui.unit.dp
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackerCalendarLayoutTest {
    @Test
    fun `month uses five rows when all days fit`() {
        assertEquals(5, calendarRowCount(YearMonth.of(2026, 6)))
    }

    @Test
    fun `month uses six rows when offset requires it`() {
        assertEquals(6, calendarRowCount(YearMonth.of(2026, 8)))
    }

    @Test
    fun `compact width is constrained by available height`() {
        val width = compactCalendarWidth(
            maxWidth = 800.dp,
            maxHeight = 240.dp,
            rowCount = 6,
            spacing = 6.dp,
            headerSpacing = 8.dp,
        )

        assertEquals(243.67f, width.value, 0.01f)
    }

    @Test
    fun `compact width never exceeds available width`() {
        val width = compactCalendarWidth(
            maxWidth = 200.dp,
            maxHeight = 500.dp,
            rowCount = 5,
            spacing = 6.dp,
            headerSpacing = 8.dp,
        )

        assertEquals(200.dp, width)
    }
}
