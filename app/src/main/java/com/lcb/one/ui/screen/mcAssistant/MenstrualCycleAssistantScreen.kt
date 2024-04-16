package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.bean.McDay
import com.lcb.one.bean.getMcDay
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.navigateSingleTop
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.toMillis
import com.lcb.one.viewmodel.MenstrualCycleViewModel
import java.time.LocalDate

const val MENSTRUAL_CYCLE_INTERVAL = 28L
const val MENSTRUAL_CYCLE_DURATION = 7L

@Composable
fun MenstrualCycleAssistantScreen() {
    val navController = LocalNav.current!!

    val mcViewmodel = viewModel<MenstrualCycleViewModel>()
    val mcDays by mcViewmodel.getAll().collectAsState(emptyList())
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
    var selectedDate by rememberSaveable { mutableLongStateOf(DateTimeUtils.nowMillis()) }
    Scaffold(
        topBar = {
            ToolBar(
                title = "经期助手",
                actions = {
                    IconButton(onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_HISTORY) }) {
                        Icon(imageVector = Icons.Rounded.History, contentDescription = "")
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !DateTimeUtils.isToday(selectedDate),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(onClick = { selectedDate = LocalDate.now().toMillis() }) {
                    Icon(imageVector = Icons.Rounded.Today, contentDescription = "")
                }
            }
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
        ) {
            Calendar(selectedDate, mcDays, predictMcDay) {
                selectedDate = it
            }

            MenstrualCycleInfo(selectedDate, mcDays, predictMcDay)

            if (!DateTimeUtils.isAfterToday(selectedDate) && runningMcDay == null && (lastMcDay == null || selectedDate > lastMcDay!!.endTime)) {
                Button(onClick = { mcViewmodel.startNewMenstrualCycle(selectedDate) }) {
                    Text(text = "开始记录经期")
                }
            }
            if (runningMcDay != null && selectedDate >= runningMcDay.startTime) {
                Button(onClick = { mcViewmodel.endMenstrualCycle(selectedDate) }) {
                    Text(text = "已结束")
                }
            }
        }
    }
}

@Composable
fun MenstrualCycleInfo(
    selectedDate: Long,
    mcDays: List<McDay>,
    predictMcDay: McDay? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = DateTimeUtils.format(selectedDate, DateTimeUtils.FORMAT_ONLY_DATE),
                    style = MaterialTheme.typography.titleLarge
                )

                val message = mcDays.getMcDay(selectedDate)?.let {
                    buildString {
                        var days = DateTimeUtils.getDurationDays(it.startTime, selectedDate)
                        append("月经期第${days + 1}天")
                        if (it.finish) {
                            days = DateTimeUtils.getDurationDays(selectedDate, it.endTime)
                            if (days > 0) {
                                append("，还有${days}天结束")
                            }
                        } else {
                            days = MENSTRUAL_CYCLE_DURATION - days
                            if (days > 0) {
                                append("，预计还有${days}天结束")
                            } else {
                                append("，已超过${MENSTRUAL_CYCLE_DURATION}天，请注意健康哦")
                            }
                        }
                    }
                } ?: "不在月经期哦"

                Text(text = message, style = MaterialTheme.typography.labelLarge)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) columnPredict@{
                Text(
                    text = "经期预测",
                    style = MaterialTheme.typography.titleLarge
                )

                if (predictMcDay == null) return@columnPredict

                val lastMcDay = mcDays.last()
                val message =
                    if (selectedDate >= lastMcDay.endTime && selectedDate < predictMcDay.startTime) {
                        val days =
                            DateTimeUtils.getDurationDays(selectedDate, predictMcDay.startTime)
                        "预计下一次在${days}天后"
                    } else if (selectedDate in predictMcDay.startTime..predictMcDay.endTime) {
                        val days =
                            DateTimeUtils.getDurationDays(predictMcDay.startTime, selectedDate)
                        "预计下一次经期的第${days + 1}天"
                    } else {
                        null
                    }

                AnimatedVisibility(message != null) {
                    Text(text = message ?: "", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
