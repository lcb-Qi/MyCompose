package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.lcb.one.util.common.DateTimeUtils.isToday
import com.lcb.one.util.common.DateTimeUtils.toMillis
import com.lcb.one.viewmodel.MenstrualCycleViewModel
import java.time.Duration

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
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        ToolBar(title = "Menstrual Cycle Assistant(Beta)", actions = {
            IconButton(onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_HISTORY) }) {
                Icon(imageVector = Icons.Rounded.History, contentDescription = "")
            }
        })
    }) { paddingValues ->
        var selectDay by remember { mutableLongStateOf(DateTimeUtils.nowMillis()) }
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
            val predictMcDay = lastMcDay?.let {
                val start = DateTimeUtils.toLocalDateTime(it.startTime)
                it.copy(
                    startTime = start.plusDays(28).toMillis(),
                    endTime = start.plusDays(28 + 7).toMillis()
                )
            }
            Calendar(selectDay, mcDays, predictMcDay) {
                selectDay = it
            }

            val text = StringBuilder().apply {
                if (runningMcDay != null) {
                    val days = Duration.between(
                        DateTimeUtils.toLocalDateTime(runningMcDay.startTime),
                        DateTimeUtils.toLocalDateTime(selectDay)
                    ).toDays() + 1
                    appendLine("今天是经期的第 $days 天")
                    if (days <= 7) {
                        appendLine("预计结束还有 ${7 - days} 天")
                    }
                }
                if (lastMcDay != null && predictMcDay != null) {
                    val days = Duration.between(
                        DateTimeUtils.toLocalDateTime(selectDay),
                        DateTimeUtils.toLocalDateTime(predictMcDay.startTime)
                    ).toDays()
                    if (days > 0) {
                        appendLine("预计下一次在 $days 天后")
                    }
                }
            }.toString()
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            if (DateTimeUtils.toLocalDateTime(selectDay).isToday() && runningMcDay == null) {
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