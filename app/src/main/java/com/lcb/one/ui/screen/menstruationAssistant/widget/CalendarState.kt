package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastAny
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.toMillis
import java.time.LocalDate

@Composable
fun rememberCalendarState(
    yearRange: IntRange = 1900..2100,
    selectedDate: LocalDate = LocalDate.now(),
    colors: CalendarColor = CalendarColor.default(),
    primaryRange: List<LongRange> = emptyList(),
    secondaryRange: List<LongRange> = emptyList(),
): CalendarState {
    return remember {
        CalendarState(selectedDate, yearRange, colors, primaryRange, secondaryRange)
    }
}


class CalendarState(
    initDate: LocalDate,
    val yearRange: IntRange,
    val colors: CalendarColor,
    primaryRange: List<LongRange> = emptyList(),
    secondaryRange: List<LongRange> = emptyList(),
) {
    companion object {
        const val DAYS_IN_WEEK = 7
        const val MONTHS_IN_YEAR = 12
    }

    var primaryRange by mutableStateOf(primaryRange)
        private set

    var secondaryRange by mutableStateOf(secondaryRange)
        private set

    var selectedMillis by mutableLongStateOf(initDate.toMillis())
        private set

    fun updateSelectedDate(millis: Long) {
        selectedMillis = millis
        selectedDate = DateTimeUtils.toLocalDate(millis)
    }

    fun updateSelectedDate(localDate: LocalDate) = updateSelectedDate(localDate.toMillis())

    var selectedDate = DateTimeUtils.toLocalDate(selectedMillis)
        private set
    val selectedYear: Int
        get() = selectedDate.year
    val selectedMonth: Int
        get() = selectedDate.monthValue
    val selectedDay: Int
        get() = selectedDate.dayOfMonth

    fun addPrimaryRange(range: List<LongRange>) {
        primaryRange = range
    }

    fun addSecondaryRange(range: List<LongRange>, clearOld: Boolean = true) {
        secondaryRange = range
    }
}

@Stable
class CalendarColor(val default: Color, val primary: Color, val secondary: Color) {
    companion object {
        @Composable
        fun default() = CalendarColor(
            default = MaterialTheme.colorScheme.surface,
            primary = MaterialTheme.colorScheme.secondaryContainer,
            secondary = MaterialTheme.colorScheme.tertiaryContainer
        )
    }

    fun containerColor(
        date: Long,
        primaryRange: List<LongRange>,
        secondaryRange: List<LongRange>
    ): Color {
        return if (primaryRange.containsValue(date)) {
            primary
        } else if (secondaryRange.containsValue(date)) {
            secondary
        } else {
            default
        }
    }
}

fun List<LongRange>.containsValue(value: Long) = fastAny { value in it }