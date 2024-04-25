package com.lcb.one.ui.screen.mcAssistant.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(modifier: Modifier = Modifier, state: CalendarState = rememberCalendarState()) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        inputs = arrayOf(state.selectedDate),
        initialPage = state.month - 1
    ) { CalendarState.MONTHS_IN_YEAR }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { state.updateYear(state.year - 1) }) {
                Icon(imageVector = Icons.Rounded.KeyboardDoubleArrowLeft, contentDescription = "")
            }

            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = state.month > 1
            ) {
                Icon(imageVector = Icons.Rounded.ChevronLeft, contentDescription = "")
            }

            Text(
                text = "${state.year}年${state.month}月",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }, enabled = state.month < CalendarState.MONTHS_IN_YEAR) {
                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "")
            }

            IconButton(onClick = { state.updateYear(state.year + 1) }) {
                Icon(imageVector = Icons.Rounded.KeyboardDoubleArrowRight, contentDescription = "")
            }
        }

        state.selectedDate = LocalDate.of(state.year, pagerState.currentPage + 1, state.dayOfMonth)
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) {
            CalendarMonth(
                modifier = Modifier.padding(horizontal = 16.dp),
                month = YearMonth.of(state.year, state.month),
                selectDay = state.dayOfMonth,
                primaryRange = state.primaryRange,
                secondaryRange = state.secondaryRange,
                colors = state.colors
            ) {
                state.updateDay(it)
            }
        }
    }
}