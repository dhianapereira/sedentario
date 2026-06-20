package io.github.dhianapereira.sedentario.ui.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.dhianapereira.sedentario.R
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import io.github.dhianapereira.sedentario.ui.components.AppHeader
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.tracker.components.EmojiOption
import io.github.dhianapereira.sedentario.ui.tracker.components.TrackerCalendar
import java.time.LocalDate

@Composable
fun TrackerRoute(
    onSettingsClick: () -> Unit,
    viewModel: TrackerViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrackerScreen(
        uiState = uiState,
        onDateSelected = viewModel::selectDate,
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
    onNewEntryClick: () -> Unit,
    onEntrySheetDismiss: () -> Unit,
    onActivitySelected: (WorkoutActivity) -> Unit,
    onSettingsClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
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

        ExtendedFloatingActionButton(
            onClick = onNewEntryClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            },
            text = { Text(stringResource(R.string.new_entry)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp),
        )
    }

    if (uiState.isEntrySheetVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = onEntrySheetDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 36.dp),
            ) {
                Text(
                    text = uiState.selectedDate.dayOfMonth.toString().padStart(2, '0'),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(18.dp))
                FlowRow(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                ) {
                    uiState.availableActivities.forEach { activity ->
                        EmojiOption(
                            activity = activity,
                            isSelected = uiState.entries[uiState.selectedDate] == activity,
                            onClick = { onActivitySelected(activity) },
                        )
                    }
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
            onNewEntryClick = {},
            onEntrySheetDismiss = {},
            onActivitySelected = {},
            onSettingsClick = {},
        )
    }
}
