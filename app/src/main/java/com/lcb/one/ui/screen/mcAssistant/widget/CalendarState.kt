package com.lcb.one.ui.screen.mcAssistant.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun rememberCalendarState(
    selectedDate: LocalDate = LocalDate.now(),
    colors: CalendarColor = CalendarColor.default(),
    primaryRange: Set<LongRange> = mutableSetOf(),
    secondaryRange: Set<LongRange> = mutableSetOf(),
): CalendarState {
    return remember {
        CalendarState(selectedDate, colors, primaryRange, secondaryRange)
    }
}


class CalendarState(
    initDate: LocalDate,
    val colors: CalendarColor,
    val primaryRange: Set<LongRange> = mutableSetOf(),
    val secondaryRange: Set<LongRange> = mutableSetOf(),
) {
    companion object {
        const val DAYS_IN_WEEK = 7
        const val MONTHS_IN_YEAR = 12
    }

    var selectedDate by mutableStateOf(initDate)
    val year
        get() = selectedDate.year
    val month
        get() = selectedDate.monthValue
    val dayOfMonth
        get() = selectedDate.dayOfMonth

    fun updateYear(newYear: Int) {
        selectedDate = LocalDate.of(newYear, month, dayOfMonth)
    }

    fun updateMonth(newMonth: Int) {
        val yearMonth = YearMonth.of(year, newMonth)
        selectedDate =
            LocalDate.of(year, newMonth, dayOfMonth.coerceAtMost(yearMonth.lengthOfMonth()))
    }

    fun updateDay(newDay: Int) {
        selectedDate = LocalDate.of(year, month, newDay)
    }

    fun addPrimaryRange(range: LongRange) {
        if (primaryRange is MutableSet) {
            primaryRange.add(range)
        }
    }

    fun addSecondaryRange(range: LongRange, clearOld: Boolean = true) {
        if (secondaryRange is MutableSet) {
            if (clearOld) secondaryRange.clear()
            secondaryRange.add(range)
        }
    }

    fun updateSelectedDate(newDate: Long) {
        this.selectedDate = DateTimeUtils.toLocalDate(newDate)
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
        primaryRange: Set<LongRange>,
        secondaryRange: Set<LongRange>
    ): Color {
        return if (primaryRange.contains(date)) {
            primary
        } else if (secondaryRange.contains(date)) {
            secondary
        } else {
            default
        }
    }
}

fun Set<LongRange>.contains(value: Long) = any { value in it }