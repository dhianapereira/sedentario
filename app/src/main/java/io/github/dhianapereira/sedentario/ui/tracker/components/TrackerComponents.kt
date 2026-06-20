package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import io.github.dhianapereira.sedentario.ui.theme.CellEmpty
import io.github.dhianapereira.sedentario.ui.theme.CellLocked
import io.github.dhianapereira.sedentario.ui.theme.Purple
import io.github.dhianapereira.sedentario.ui.theme.PurpleDark
import io.github.dhianapereira.sedentario.ui.theme.PurpleLight
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TrackerHeader(onNewEntryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(7.dp)),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Sedentário",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
            )
        }
        NewEntryButton(onClick = onNewEntryClick)
    }
}

@Composable
private fun NewEntryButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .border(1.dp, PurpleLight, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "+",
            color = PurpleLight,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "Novo",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun WeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf("D", "S", "T", "Q", "Q", "S", "S").forEach { label ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    color = Color(0xFFA6A2AE),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun TrackerMonthGrid(
    month: YearMonth,
    today: LocalDate,
    entries: Map<LocalDate, WorkoutActivity>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    val firstDayOffset = month.atDay(1).dayOfWeek.value % 7
    val cells = firstDayOffset + month.lengthOfMonth()
    val rows = (cells + 6) / 7

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(rows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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

@Composable
private fun DayCell(
    activity: WorkoutActivity?,
    isToday: Boolean,
    isSelected: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(7.dp)
    val background = when {
        isLocked -> CellLocked
        activity != null -> Purple
        else -> CellEmpty
    }
    val border = when {
        isToday -> BorderStroke(3.dp, Color.White)
        isSelected -> BorderStroke(2.dp, PurpleLight)
        isLocked -> BorderStroke(1.dp, Color(0xFF202127))
        else -> BorderStroke(1.dp, Color(0xFF2B2C33))
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(background)
            .border(border, shape)
            .clickable(enabled = !isLocked, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        activity?.let {
            Text(text = it.emoji, fontSize = 18.sp)
        }
    }
}

@Composable
fun EmojiOption(
    activity: WorkoutActivity,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .size(58.dp)
            .semantics { contentDescription = activity.description }
            .clip(shape)
            .background(if (isSelected) PurpleDark else CellEmpty)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PurpleLight else Color(0xFF2B2C33),
                shape = shape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = activity.emoji, fontSize = 28.sp)
    }
}
