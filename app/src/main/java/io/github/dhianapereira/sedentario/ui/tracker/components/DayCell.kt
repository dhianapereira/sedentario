package io.github.dhianapereira.sedentario.ui.tracker.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val targetBackground = when {
        isLocked -> MaterialTheme.colorScheme.surface
        activity != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val targetBorderColor = when {
        isToday -> MaterialTheme.colorScheme.onBackground
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val targetBorderWidth = when {
        isToday -> 3.dp
        isSelected -> 2.dp
        else -> 1.dp
    }
    val background by animateColorAsState(
        targetValue = targetBackground,
        animationSpec = tween(220),
        label = "day background",
    )
    val borderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(220),
        label = "day border color",
    )
    val borderWidth by animateDpAsState(
        targetValue = targetBorderWidth,
        animationSpec = tween(220),
        label = "day border width",
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(background)
            .border(BorderStroke(borderWidth, borderColor), shape)
            .clickable(enabled = !isLocked, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = activity,
            transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(120)) },
            label = "activity emoji",
        ) { currentActivity ->
            currentActivity?.let {
                Text(text = it.emoji, fontSize = 18.sp)
            }
        }
    }
}
