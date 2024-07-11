package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.common.AppIconButton
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(modifier: Modifier = Modifier, state: CalendarState = rememberCalendarState()) {
    val firstDay = remember { LocalDate.of(state.yearRange.first, 1, 1) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollToNext: (indexCount: Int) -> Unit = {
        scope.launch { listState.scrollToItem(listState.firstVisibleItemIndex + it) }
    }

    LaunchedEffect(state.selectedMillis) {
        listState.scrollToItem((state.selectedYear - state.yearRange.first) * 12 + state.selectedMonth - 1)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppIconButton(
                icon = Icons.Rounded.KeyboardDoubleArrowLeft,
                onClick = { scrollToNext(-12) }
            )

            AppIconButton(
                icon = Icons.Rounded.ChevronLeft,
                onClick = { scrollToNext(-1) }
            )

            Text(
                text = "${state.selectedYear}年${state.selectedMonth}月",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            AppIconButton(
                icon = Icons.Rounded.ChevronRight,
                onClick = { scrollToNext(1) }
            )

            AppIconButton(
                icon = Icons.Rounded.KeyboardDoubleArrowRight,
                onClick = { scrollToNext(12) }
            )
        }

        LazyRow(
            state = listState,
            flingBehavior = rememberMySnapFlingBehavior(lazyListState = listState)
        ) {
            items((state.yearRange.last - state.yearRange.first + 1) * 12, key = { it }) { index ->
                Box(modifier = Modifier.fillParentMaxWidth()) {
                    val date = firstDay.plusMonths(index.toLong())
                    CalendarMonth(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        month = YearMonth.of(date.year, date.month),
                        selectMillis = state.selectedMillis,
                        primaryRange = state.primaryRange,
                        secondaryRange = state.secondaryRange,
                        colors = state.colors,
                        onDateSelected = { state.updateSelectedDate(it) }
                    )
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }.collect {
                val index = it
                val yearMonth = YearMonth.of(index / 12 + state.yearRange.first, index % 12 + 1)
                val day = state.selectedDate.dayOfMonth.coerceAtMost(yearMonth.lengthOfMonth())
                state.updateSelectedDate(LocalDate.of(yearMonth.year, yearMonth.monthValue, day))
            }
        }
    }
}

@Composable
private fun rememberMySnapFlingBehavior(
    lazyListState: LazyListState,
    decayAnimationSpec: DecayAnimationSpec<Float> = exponentialDecay()
): FlingBehavior {
    return remember(decayAnimationSpec, lazyListState) {
        val original = SnapLayoutInfoProvider(lazyListState)
        val snapLayoutInfoProvider = object : SnapLayoutInfoProvider by original {
            override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float {
                return 0.0f
            }
        }

        snapFlingBehavior(
            snapLayoutInfoProvider = snapLayoutInfoProvider,
            decayAnimationSpec = decayAnimationSpec,
            snapAnimationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
    }
}