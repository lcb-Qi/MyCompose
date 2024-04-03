package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.constraintlayout.compose.ConstraintLayout
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

private const val DAYS_IN_WEEK = 7
private const val MONTHS_IN_YEAR = 12

@Composable
private fun CalendarDay(
    modifier: Modifier = Modifier, day: Int, selected: Boolean, onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        selected = selected,
        onClick = onClick,
        border = if (selected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = day.toString(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CalendarMonth(
    month: YearMonth, selectDay: Int = 1, onDaySelected: (Int) -> Unit = {}
) {
    LLog.d("TAG", "CalendarMonth: month = $month")
    ConstraintLayout {
        val (weekRef, contentRef) = createRefs()
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek.value
        Row(modifier = Modifier
            .constrainAs(weekRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth()
        ) {
            val dayNames = arrayListOf<Pair<String, String>>()
            val weekdays = with(Locale.getDefault()) {
                DayOfWeek.values().map {
                    it.getDisplayName(TextStyle.FULL, this) to it.getDisplayName(
                        TextStyle.NARROW, this
                    )
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
                    text = it.second,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        var cellIndex = 0
        Column(modifier = Modifier
            .constrainAs(contentRef) {
                top.linkTo(weekRef.bottom)
                start.linkTo(weekRef.start)
                end.linkTo(weekRef.end)
            }
            .wrapContentSize()) {
            val difference = month.atDay(1).dayOfWeek.value - firstDayOfWeek
            val daysFromStartOfWeekToFirstOfMonth = if (difference < 0) {
                difference + DAYS_IN_WEEK
            } else {
                difference
            }
            for (weekIndex in 0 until 6) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (dayIndex in 0 until DAYS_IN_WEEK) {
                        if (cellIndex < daysFromStartOfWeekToFirstOfMonth || cellIndex >= (daysFromStartOfWeekToFirstOfMonth + month.lengthOfMonth())) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val dayNumber = cellIndex - daysFromStartOfWeekToFirstOfMonth + 1
                            CalendarDay(
                                modifier = Modifier
                                    .weight(1f)
                                    .requiredSize(48.dp),
                                day = dayNumber,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(default: Long = DateTimeUtils.nowMillis(), onDateChanged: (Long) -> Unit) {
    val dateTime = DateTimeUtils.toLocalDateTime(default)

    val pagerState = rememberPagerState(initialPage = dateTime.monthValue - 1) { MONTHS_IN_YEAR }
    var selectDay by remember { mutableIntStateOf(dateTime.dayOfMonth) }
    var selectYear by remember { mutableIntStateOf(dateTime.year) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { selectYear-- }) {
                Icon(imageVector = Icons.Rounded.ChevronLeft, contentDescription = "")
            }

            Text(
                text = "${selectYear}年${pagerState.currentPage + 1}月",
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(onClick = { selectYear++ }) {
                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "")
            }
        }

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { pagerIndex ->
            val yearMonth = YearMonth.of(selectYear, pagerIndex + 1)

            CalendarMonth(month = yearMonth, selectDay = selectDay) {
                selectDay = it
                onDateChanged(yearMonth.atDayMillis(selectDay))
            }

            if (pagerState.currentPage == pagerIndex) {
                selectDay = selectDay.coerceAtMost(yearMonth.lengthOfMonth())
                onDateChanged(yearMonth.atDayMillis(selectDay))
            }
        }
    }
}

private fun YearMonth.atDayMillis(day: Int): Long {
    require(day in 0..lengthOfMonth()) {
        "$year-$monthValue-$day is invalid, day must in ${0..lengthOfMonth()}"
    }

    return atDay(day).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}