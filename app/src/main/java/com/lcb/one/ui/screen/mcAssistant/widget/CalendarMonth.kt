package com.lcb.one.ui.screen.mcAssistant.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.fastForEach
import com.lcb.one.ui.screen.mcAssistant.repo.model.McDay
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
    month: YearMonth,
    selectDay: Int = 1,
    mcData: List<McDay> = emptyList(),
    predictMcDay: McDay? = null,
    onDaySelected: (Int) -> Unit
) {
    Column {
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
                difference + DAYS_IN_WEEK
            } else {
                difference
            }
            for (weekIndex in 0 until 6) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (dayIndex in 0 until DAYS_IN_WEEK) {
                        if (cellIndex < daysFromStartOfWeekToFirstOfMonth ||
                            cellIndex >= (daysFromStartOfWeekToFirstOfMonth + month.lengthOfMonth())
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val dayNumber = cellIndex - daysFromStartOfWeekToFirstOfMonth + 1
                            CalendarDay(
                                modifier = Modifier.weight(1f),
                                day = dayNumber,
                                mcDay = mcData.any {
                                    if (it.finish) {
                                        month.atDayMillis(dayNumber) in it.startTime..it.endTime
                                    } else {
                                        month.atDayMillis(dayNumber) in it.startTime..LocalDate.now()
                                            .toMillis()
                                    }
                                },
                                predictMcDay = if (predictMcDay == null) {
                                    false
                                } else {
                                    month.atDayMillis(dayNumber) in predictMcDay.startTime..predictMcDay.endTime
                                },
                                selected = selectDay == dayNumber
                            ) {
                                onDaySelected(dayNumber)
                            }
                        }

                        cellIndex++
                    }
                }
            }
        }
    }
}