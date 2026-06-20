package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.dhianapereira.sedentario.model.WorkoutActivity

@Composable
internal fun DayCell(
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
