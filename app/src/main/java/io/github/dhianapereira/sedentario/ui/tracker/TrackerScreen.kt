package io.github.dhianapereira.sedentario.ui.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import io.github.dhianapereira.sedentario.model.WorkoutActivityCategory
import io.github.dhianapereira.sedentario.ui.components.AppHeader
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.tracker.components.EmojiOption
import io.github.dhianapereira.sedentario.ui.tracker.components.TrackerCalendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TrackerRoute(
    onSettingsClick: () -> Unit,
    viewModel: TrackerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrackerScreen(
        uiState = uiState,
        onDateSelected = viewModel::selectDate,
        onPreviousMonth = viewModel::showPreviousMonth,
        onNextMonth = viewModel::showNextMonth,
        onNewEntryClick = viewModel::showEntrySheet,
        onEntrySheetDismiss = viewModel::hideEntrySheet,
        onActivitySelected = viewModel::selectActivity,
        onSettingsClick = onSettingsClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TrackerScreen(
    uiState: TrackerUiState,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onNewEntryClick: () -> Unit,
    onEntrySheetDismiss: () -> Unit,
    onActivitySelected: (WorkoutActivity) -> Unit,
    onSettingsClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val selectedActivity = uiState.selectedDate?.let(uiState.entries::get)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
            ) {
                val compactLayout = maxWidth > maxHeight
                val horizontalPadding = if (compactLayout) 16.dp else 28.dp
                val verticalPadding = if (compactLayout) 8.dp else 24.dp
                val headerSpacing = if (compactLayout) 8.dp else 26.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = verticalPadding,
                        ),
                ) {
                    AppHeader(onSettingsClick = onSettingsClick)
                    Spacer(modifier = Modifier.height(headerSpacing))
                    TrackerCalendar(
                        month = uiState.displayedMonth,
                        today = uiState.today,
                        entries = uiState.entries,
                        selectedDate = uiState.selectedDate,
                        onDateSelected = onDateSelected,
                        onPreviousMonth = onPreviousMonth,
                        onNextMonth = onNextMonth,
                        compactLayout = compactLayout,
                        modifier = if (compactLayout) {
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        } else {
                            Modifier.fillMaxWidth()
                        },
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.canAddEntry,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp),
            enter = fadeIn() + scaleIn(initialScale = 0.85f),
            exit = fadeOut() + scaleOut(targetScale = 0.85f),
        ) {
            ExtendedFloatingActionButton(
                onClick = onNewEntryClick,
                icon = {
                    Icon(
                        imageVector = if (selectedActivity == null) {
                            Icons.Default.Add
                        } else {
                            Icons.Default.Edit
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                text = {
                    Text(
                        stringResource(
                            if (selectedActivity == null) R.string.new_entry else R.string.edit_entry,
                        ),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(18.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                ),
            )
        }
    }

    val selectedDate = uiState.selectedDate
    if (uiState.isEntrySheetVisible && selectedDate != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val selectedActivity = uiState.entries[selectedDate]
        val locale = LocalConfiguration.current.locales[0]
        val formattedDate = selectedDate.format(
            DateTimeFormatter.ofPattern("dd MMMM yyyy", locale),
        )
        val activitiesByCategory = uiState.availableActivities.groupBy { it.category }
        val firstCategory = WorkoutActivityCategory.entries.first { category ->
            activitiesByCategory[category].orEmpty().isNotEmpty()
        }
        var selectedCategory by remember(selectedActivity) {
            mutableStateOf(selectedActivity?.category ?: firstCategory)
        }
        var isCategoryMenuExpanded by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = onEntrySheetDismiss,
            modifier = Modifier.statusBarsPadding(),
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 36.dp),
            ) {
                Text(
                    text = stringResource(
                        if (selectedActivity == null) R.string.new_activity else R.string.edit_activity,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedDate,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = stringResource(R.string.activity_category),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { isCategoryMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = stringResource(selectedCategory.labelRes),
                                fontWeight = FontWeight.SemiBold,
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = isCategoryMenuExpanded,
                        onDismissRequest = { isCategoryMenuExpanded = false },
                    ) {
                        WorkoutActivityCategory.entries.forEach { category ->
                            if (activitiesByCategory[category].orEmpty().isNotEmpty()) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(category.labelRes)) },
                                    onClick = {
                                        selectedCategory = category
                                        isCategoryMenuExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                activitiesByCategory[selectedCategory].orEmpty().let { activities ->
                    if (activities.isNotEmpty()) {
                        Text(
                            text = stringResource(selectedCategory.labelRes),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            activities.forEach { activity ->
                                EmojiOption(
                                    activity = activity,
                                    isSelected = selectedActivity == activity,
                                    onClick = { onActivitySelected(activity) },
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = selectedActivity != null) {
                    Text(
                        text = stringResource(R.string.tap_again_to_remove),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
            }
        }
    }
}

@Preview(name = "Dark theme", showBackground = true)
@Composable
private fun DarkTrackerScreenPreview() {
    TrackerScreenPreview(darkTheme = true)
}

@Preview(name = "Light theme", showBackground = true)
@Composable
private fun LightTrackerScreenPreview() {
    TrackerScreenPreview(darkTheme = false)
}

@Preview(
    name = "Landscape",
    widthDp = 800,
    heightDp = 360,
    showBackground = true,
)
@Composable
private fun LandscapeTrackerScreenPreview() {
    TrackerScreenPreview(darkTheme = true)
}

@Composable
private fun TrackerScreenPreview(darkTheme: Boolean) {
    val today = LocalDate.of(2026, 6, 20)
    SedentarioTheme(darkTheme = darkTheme) {
        TrackerScreen(
            uiState = TrackerUiState(today = today),
            onDateSelected = {},
            onPreviousMonth = {},
            onNextMonth = {},
            onNewEntryClick = {},
            onEntrySheetDismiss = {},
            onActivitySelected = {},
            onSettingsClick = {},
        )
    }
}
