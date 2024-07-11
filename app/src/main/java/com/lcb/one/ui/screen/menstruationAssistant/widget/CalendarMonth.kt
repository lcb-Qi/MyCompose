package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.lcb.one.util.common.atDayMillis
import com.lcb.one.util.common.toMillis
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarMonth(
    modifier: Modifier = Modifier,
    month: YearMonth,
    selectMillis: Long,
    primaryRange: List<LongRange> = emptyList(),
    secondaryRange: LongRange? = null,
    colors: CalendarColor,
    onDateSelected: (Long) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek.value
        WeekHeadLine(firstDayOfWeek = firstDayOfWeek)

        var cellIndex = 0
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val difference = month.atDay(1).dayOfWeek.value - firstDayOfWeek
            val daysFromStartOfWeekToFirstOfMonth = if (difference < 0) {
                difference + CalendarState.DAYS_IN_WEEK
            } else {
                difference
            }
            for (weekIndex in 0 until 6) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (dayIndex in 0 until CalendarState.DAYS_IN_WEEK) {
                        if (cellIndex < daysFromStartOfWeekToFirstOfMonth ||
                            cellIndex >= (daysFromStartOfWeekToFirstOfMonth + month.lengthOfMonth())
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val dayNumber = cellIndex - daysFromStartOfWeekToFirstOfMonth + 1
                            val currentDate = month.atDayMillis(dayNumber)
                            val containerColor =
                                colors.containerColor(currentDate, primaryRange, secondaryRange)
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                selected = currentDate == selectMillis,
                                color = containerColor,
                                onClick = { onDateSelected(currentDate) },
                                content = {
                                    val style = if (currentDate == LocalDate.now().toMillis()) {
                                        LocalTextStyle.current.run {
                                            copy(
                                                color = MaterialTheme.colorScheme.error,
                                                fontSize = (this.fontSize.value * 1.5).sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    } else {
                                        LocalTextStyle.current
                                    }
                                    Text(
                                        text = dayNumber.toString(),
                                        textAlign = TextAlign.Center,
                                        style = style
                                    )
                                }
                            )
                        }

                        cellIndex++
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekHeadLine(modifier: Modifier = Modifier, firstDayOfWeek: Int) {
    Row(modifier = modifier.fillMaxWidth()) {
        val dayNames = arrayListOf<String>()
        val weekdays = DayOfWeek.entries.map {
            it.getDisplayName(TextStyle.NARROW, Locale.getDefault())
        }

        for (i in firstDayOfWeek - 1 until weekdays.size) {
            dayNames.add(weekdays[i])
        }
        for (i in 0 until firstDayOfWeek - 1) {
            dayNames.add(weekdays[i])
        }
        dayNames.fastForEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}