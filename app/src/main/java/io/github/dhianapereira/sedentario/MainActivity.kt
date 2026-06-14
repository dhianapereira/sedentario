package io.github.dhianapereira.sedentario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.launch

private val Background = Color(0xFF060607)
private val CellEmpty = Color(0xFF1A1B1F)
private val CellLocked = Color(0xFF101114)
private val Purple = Color(0xFF7C3AED)
private val PurpleLight = Color(0xFF8B5CF6)
private val PurpleDark = Color(0xFF4C1D95)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SedentarioTheme {
                WorkoutTrackerScreen()
            }
        }
    }
}

@Composable
private fun SedentarioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Background,
            surface = Color(0xFF111217),
            primary = PurpleLight,
            onBackground = Color.White,
            onSurface = Color.White,
        ),
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun WorkoutTrackerScreen() {
    val today = remember { LocalDate.now() }
    val month = remember(today) { YearMonth.from(today) }
    val entries = remember { mutableStateMapOf<LocalDate, String>() }
    val emojiOptions = remember { listOf("🏋️", "🏃", "⚽", "🚶", "🧘", "😴", "🛌", "❌") }

    var selectedDate by remember { mutableStateOf(today) }
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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
            Header(onNewClick = { isSheetOpen = true })
            Spacer(modifier = Modifier.height(26.dp))
            WeekHeader()
            Spacer(modifier = Modifier.height(12.dp))
            MonthGrid(
                month = month,
                today = today,
                entries = entries,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                },
            )
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            containerColor = Color(0xFF111217),
            contentColor = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 36.dp),
            ) {
                Text(
                    text = selectedDate.dayOfMonth.toString().padStart(2, '0'),
                    color = Color(0xFFA9A7B3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(18.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    emojiOptions.forEach { emoji ->
                        EmojiOption(
                            emoji = emoji,
                            isSelected = entries[selectedDate] == emoji,
                            onClick = {
                                entries[selectedDate] = emoji
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    isSheetOpen = false
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(onNewClick: () -> Unit) {
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
        NewEntryButton(onClick = onNewClick)
    }
}

@Composable
private fun NewEntryButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, PurpleLight, RoundedCornerShape(8.dp))
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
private fun WeekHeader() {
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
private fun MonthGrid(
    month: YearMonth,
    today: LocalDate,
    entries: Map<LocalDate, String>,
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
                    val index = row * 7 + column
                    val day = index - firstDayOffset + 1
                    if (day in 1..month.lengthOfMonth()) {
                        val date = month.atDay(day)
                        DayCell(
                            date = date,
                            emoji = entries[date],
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
    date: LocalDate,
    emoji: String?,
    isToday: Boolean,
    isSelected: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(7.dp)
    val background = when {
        isLocked -> CellLocked
        emoji != null -> Purple
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
        if (emoji != null) {
            Text(text = emoji, fontSize = 18.sp)
        }
    }
}

@Composable
private fun EmojiOption(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .size(58.dp)
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
        Text(text = emoji, fontSize = 28.sp)
    }
}

@Preview
@Composable
private fun WorkoutTrackerScreenPreview() {
    SedentarioTheme {
        WorkoutTrackerScreen()
    }
}
