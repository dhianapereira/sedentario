package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TrackerHeader(
    appTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Sedentário",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.weight(1f),
        )
        ThemeMenuButton(
            appTheme = appTheme,
            onThemeSelected = onThemeSelected,
        )
    }
}

@Composable
private fun ThemeMenuButton(
    appTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(8.dp)
    val primary = MaterialTheme.colorScheme.primary

    Box {
        Row(
            modifier = Modifier
                .clip(shape)
                .border(1.dp, primary, shape)
                .clickable { isMenuOpen = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = if (appTheme == AppTheme.DARK) {
                    Icons.Outlined.DarkMode
                } else {
                    Icons.Outlined.LightMode
                },
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = if (appTheme == AppTheme.DARK) "Escuro" else "Claro",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false },
        ) {
            ThemeMenuItem(
                label = "Escuro",
                theme = AppTheme.DARK,
                selectedTheme = appTheme,
                onClick = {
                    isMenuOpen = false
                    onThemeSelected(AppTheme.DARK)
                },
            )
            ThemeMenuItem(
                label = "Claro",
                theme = AppTheme.LIGHT,
                selectedTheme = appTheme,
                onClick = {
                    isMenuOpen = false
                    onThemeSelected(AppTheme.LIGHT)
                },
            )
        }
    }
}

@Composable
private fun ThemeMenuItem(
    label: String,
    theme: AppTheme,
    selectedTheme: AppTheme,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(label) },
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = if (theme == AppTheme.DARK) {
                    Icons.Outlined.DarkMode
                } else {
                    Icons.Outlined.LightMode
                },
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (theme == selectedTheme) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                )
            }
        },
    )
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        isLocked -> MaterialTheme.colorScheme.surface
        activity != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val border = when {
        isToday -> BorderStroke(3.dp, MaterialTheme.colorScheme.onBackground)
        isSelected -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        isLocked -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = shape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = activity.emoji, fontSize = 28.sp)
    }
}
