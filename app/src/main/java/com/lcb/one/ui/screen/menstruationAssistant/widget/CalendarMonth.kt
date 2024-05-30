package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.lcb.one.util.common.atDayMillis
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarMonth(
    modifier: Modifier = Modifier,
    month: YearMonth,
    selectDay: Int = 1,
    primaryRange: Set<LongRange> = emptySet(),
    secondaryRange: Set<LongRange> = emptySet(),
    colors: CalendarColor,
    onDaySelected: (Int) -> Unit
) {
    Column(modifier = modifier) {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek.value
        Row(modifier = Modifier.fillMaxWidth()) {
            val dayNames = arrayListOf<String>()
            val weekdays = with(Locale.getDefault()) {
                DayOfWeek.values().map {
                    it.getDisplayName(TextStyle.NARROW, this)
                }
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

        var cellIndex = 0
        Column(modifier = Modifier.wrapContentSize()) {
            val difference = month.atDay(1).dayOfWeek.value - firstDayOfWeek
            val daysFromStartOfWeekToFirstOfMonth = if (difference < 0) {
                difference + CalendarState.DAYS_IN_WEEK
            } else {
                difference
            }
            for (weekIndex in 0 until 6) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (dayIndex in 0 until CalendarState.DAYS_IN_WEEK) {
                        if (cellIndex < daysFromStartOfWeekToFirstOfMonth ||
                            cellIndex >= (daysFromStartOfWeekToFirstOfMonth + month.lengthOfMonth())
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val dayNumber = cellIndex - daysFromStartOfWeekToFirstOfMonth + 1
                            val currentDate = month.atDayMillis(dayNumber)
                            val containerColor = colors.containerColor(
                                currentDate,
                                primaryRange,
                                secondaryRange
                            )
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                selected = currentDate == month.atDayMillis(selectDay),
                                color = containerColor,
                                onClick = { onDaySelected(dayNumber) }
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    textAlign = TextAlign.Center,
                                )
                                if (secondaryRange.contains(currentDate)) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        text = "预",
                                        textAlign = TextAlign.End,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 8.sp,
                                            color = containerColor.textColor(),
                                            lineHeight = 8.sp
                                        )
                                    )
                                }
                            }
                        }

                        cellIndex++
                    }
                }
            }
        }
    }
}

fun Color.textColor(): Color {
    return if (convert(ColorSpaces.Srgb).luminance() > 0.5) Color.Black else Color.White
}