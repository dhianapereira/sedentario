package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TrackerCalendar(
    month: YearMonth,
    today: LocalDate,
    entries: Map<LocalDate, WorkoutActivity>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    compactLayout: Boolean,
    modifier: Modifier = Modifier,
) {
    val rowCount = calendarRowCount(month)
    val spacing = if (compactLayout) 6.dp else 8.dp
    val headerSpacing = if (compactLayout) 8.dp else 12.dp
    val monthHeaderSpacing = if (compactLayout) 4.dp else 12.dp

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
                monthHeaderHeight = 48.dp + monthHeaderSpacing,
            )
        } else {
            maxWidth
        }

        Column(modifier = Modifier.width(calendarWidth)) {
            MonthNavigationHeader(
                month = month,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
            )
            Spacer(modifier = Modifier.height(monthHeaderSpacing))
            WeekHeader(horizontalSpacing = spacing)
            Spacer(modifier = Modifier.height(headerSpacing))
            AnimatedContent(
                targetState = month,
                modifier = Modifier.fillMaxWidth(),
                transitionSpec = {
                    val forward = targetState.isAfter(initialState)
                    val enter = slideInHorizontally(
                        animationSpec = tween(280, easing = FastOutSlowInEasing),
                        initialOffsetX = { width -> if (forward) width / 3 else -width / 3 },
                    ) + fadeIn(tween(220))
                    val exit = slideOutHorizontally(
                        animationSpec = tween(280, easing = FastOutSlowInEasing),
                        targetOffsetX = { width -> if (forward) -width / 3 else width / 3 },
                    ) + fadeOut(tween(180))
                    (enter togetherWith exit).using(SizeTransform(clip = false))
                },
                label = "month transition",
            ) { animatedMonth ->
                TrackerMonthGrid(
                    month = animatedMonth,
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
}

@Composable
private fun MonthNavigationHeader(
    month: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    val locale = LocalConfiguration.current.locales[0]
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", locale)
    val monthLabel = month.format(formatter).replaceFirstChar { it.titlecase(locale) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.previous_month),
            )
        }
        AnimatedContent(
            targetState = monthLabel,
            modifier = Modifier.weight(1f),
            transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(120)) },
            label = "month label",
        ) { label ->
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        IconButton(
            onClick = onNextMonth,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = stringResource(R.string.next_month),
            )
        }
    }
}
