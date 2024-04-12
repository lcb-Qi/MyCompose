package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.lcb.one.bean.McDay
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.atDayMillis
import java.time.YearMonth

const val DAYS_IN_WEEK = 7
const val MONTHS_IN_YEAR = 12

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    default: Long = DateTimeUtils.nowMillis(),
    mcData: List<McDay> = emptyList(),
    predictMcDay: McDay? = null,
    onDateChanged: (Long) -> Unit,
) {
    // TODO: 提取状态，给外部提供 rememberXXXState()，参数仅传XXXState
    val dateTime = DateTimeUtils.toLocalDateTime(default)

    val pagerState = rememberPagerState(
        inputs = arrayOf(default),
        initialPage = dateTime.monthValue - 1
    ) { MONTHS_IN_YEAR }
    var selectDay by remember(default) { mutableIntStateOf(dateTime.dayOfMonth) }
    var selectYear by remember(default) { mutableIntStateOf(dateTime.year) }

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

            CalendarMonth(
                month = yearMonth,
                selectDay = selectDay,
                mcData = mcData,
                predictMcDay = predictMcDay
            ) {
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