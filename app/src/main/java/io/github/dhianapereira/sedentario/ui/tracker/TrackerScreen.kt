package io.github.dhianapereira.sedentario.ui.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import io.github.dhianapereira.sedentario.ui.theme.Background
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.theme.Surface
import io.github.dhianapereira.sedentario.ui.tracker.components.EmojiOption
import io.github.dhianapereira.sedentario.ui.tracker.components.TrackerHeader
import io.github.dhianapereira.sedentario.ui.tracker.components.TrackerMonthGrid
import io.github.dhianapereira.sedentario.ui.tracker.components.WeekHeader
import java.time.LocalDate

@Composable
fun TrackerRoute(viewModel: TrackerViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrackerScreen(
        uiState = uiState,
        onDateSelected = viewModel::selectDate,
        onNewEntryClick = viewModel::showEntrySheet,
        onEntrySheetDismiss = viewModel::hideEntrySheet,
        onActivitySelected = viewModel::selectActivity,
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
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 28.dp, vertical = 24.dp),
        ) {
            TrackerHeader(onNewEntryClick = onNewEntryClick)
            Spacer(modifier = Modifier.height(26.dp))
            WeekHeader()
            Spacer(modifier = Modifier.height(12.dp))
            TrackerMonthGrid(
                month = uiState.displayedMonth,
                today = uiState.today,
                entries = uiState.entries,
                selectedDate = uiState.selectedDate,
                onDateSelected = onDateSelected,
            )
        }
    }

    if (uiState.isEntrySheetVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = onEntrySheetDismiss,
            sheetState = sheetState,
            containerColor = Surface,
            contentColor = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 36.dp),
            ) {
                Text(
                    text = uiState.selectedDate.dayOfMonth.toString().padStart(2, '0'),
                    color = Color(0xFFA9A7B3),
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

@Preview(showBackground = true)
@Composable
private fun TrackerScreenPreview() {
    val today = LocalDate.of(2026, 6, 20)
    SedentarioTheme {
        TrackerScreen(
            uiState = TrackerUiState(today = today),
            onDateSelected = {},
            onNewEntryClick = {},
            onEntrySheetDismiss = {},
            onActivitySelected = {},
        )
    }
}
