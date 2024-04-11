package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lcb.one.bean.McDay
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.navigateSingleTop
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.DateTimeUtils.isBeforeToday
import com.lcb.one.util.common.DateTimeUtils.isToday
import com.lcb.one.util.common.DateTimeUtils.toMillis
import com.lcb.one.util.common.isBeforeToday
import com.lcb.one.util.common.isToday
import com.lcb.one.util.common.toMillis
import com.lcb.one.viewmodel.MenstrualCycleViewModel
import java.time.Duration

const val MENSTRUAL_CYCLE_INTERVAL = 28L
const val MENSTRUAL_CYCLE_DURATION = 7L

@Composable
fun MenstrualCycleAssistantScreen(navController: NavController) {
    val mcViewmodel = viewModel<MenstrualCycleViewModel>()
    val mcDays by mcViewmodel.mcDaysFlow.collectAsState(emptyList())
    val runningMcDay = mcDays.find { !it.finish }
    var lastMcDay: McDay? = null
    mcDays.forEach {
        if (lastMcDay == null) {
            lastMcDay = it
        } else if (it.startTime >= lastMcDay!!.startTime) {
            lastMcDay = it
        }
    }
    val predictMcDay = lastMcDay?.let {
        val start = DateTimeUtils.toLocalDateTime(it.startTime)
        it.copy(
            startTime = start.plusDays(MENSTRUAL_CYCLE_INTERVAL).toMillis(),
            endTime = start.plusDays(MENSTRUAL_CYCLE_INTERVAL + MENSTRUAL_CYCLE_DURATION - 1)
                .toMillis()
        )
    }
    Scaffold(
        topBar = {
            ToolBar(title = "经期助手", actions = {
                IconButton(onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_HISTORY) }) {
                    Icon(imageVector = Icons.Rounded.History, contentDescription = "")
                }
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var selectDay by remember { mutableLongStateOf(DateTimeUtils.nowMillis()) }
            Calendar(selectDay, mcDays, predictMcDay) { selectDay = it }

            val selectDateTime = DateTimeUtils.toLocalDateTime(selectDay)
            if (!selectDateTime.isBeforeToday()) {
                MessageText(selectDay, runningMcDay, predictMcDay)
            }

            if (selectDateTime.isToday() && runningMcDay == null) {
                Button(onClick = { mcViewmodel.startNewMenstrualCycle() }) {
                    Text(text = "开始记录经期")
                }
            }
            if (runningMcDay != null && selectDay >= runningMcDay.startTime) {
                Button(onClick = { mcViewmodel.endMenstrualCycle(selectDay) }) {
                    Text(text = "已结束")
                }
            }
        }
    }
}

@Composable
fun MessageText(
    selectDay: Long,
    runningMcDay: McDay? = null,
    predictMcDay: McDay? = null
) {
    ProvideTextStyle(MaterialTheme.typography.titleMedium) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            runningMcDay?.let {
                val days = DateTimeUtils.getDurationDays(it.startTime, selectDay).toDays()
                Text(text = "今天是经期的第 ${days + 1} 天")

                if (days <= MENSTRUAL_CYCLE_DURATION) {
                    Text(text = "预计结束还有 ${MENSTRUAL_CYCLE_DURATION - days} 天")
                }
            }

            predictMcDay?.let {
                val days = DateTimeUtils.getDurationDays(selectDay, it.startTime).toDays()
                if (days > 0) {
                    Text(text = "预计下一次在 $days 天后")
                }

                if (selectDay in it.startTime..it.endTime) {
                    val predictDays =
                        DateTimeUtils.getDurationDays(it.startTime, selectDay).toDays()
                    Text(text = "预计下一次经期的第 $predictDays 天")
                }
            }
        }
    }
}
