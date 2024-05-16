package com.lcb.one.ui.screen.menstruationAssistant

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
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.localization.Localization
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.getMcDay
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.navigateSingleTop
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.toMillis
import com.lcb.one.ui.screen.menstruationAssistant.repo.MenstruationViewModel
import com.lcb.one.ui.screen.menstruationAssistant.widget.Calendar
import com.lcb.one.ui.screen.menstruationAssistant.widget.rememberCalendarState
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.ui.widget.common.AppIconButton
import java.time.LocalDate

private const val MENSTRUATION_INTERVAL = 28L// 两次月经间隔
private const val MENSTRUATION_DURATION = 7L// 月经持续时间

@Composable
fun MenstruationAssistantScreen() {
    val navController = LocalNav.current!!

    val mcViewmodel = viewModel<MenstruationViewModel>()
    val mcDays by mcViewmodel.getAll().collectAsState(emptyList())
    val runningMcDay = mcDays.find { !it.finish }
    var lastMcDay: MenstruationDay? = null
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
            finish = true,
            startTime = start.plusDays(MENSTRUATION_INTERVAL).toMillis(),
            endTime = start.plusDays(MENSTRUATION_INTERVAL + MENSTRUATION_DURATION - 1)
                .toMillis()
        )
    }
    val state = rememberCalendarState()
    val selectedDate = state.selectedDate.toMillis()
    Scaffold(
        topBar = {
            ToolBar(
                title = Localization.menstrualAssistant,
                actions = {
                    AppIconButton(
                        icon = Icons.Rounded.History,
                        onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_HISTORY) }
                    )
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !DateTimeUtils.isToday(selectedDate),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(onClick = {
                    state.updateSelectedDate(LocalDate.now().toMillis())
                }) {
                    Icon(imageVector = Icons.Rounded.Today, contentDescription = "")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mcDays.forEach {
                state.addPrimaryRange(it.toRange())
            }
            predictMcDay?.let { state.addSecondaryRange(it.toRange()) }
            Calendar(state = state)

            MenstrualCycleInfo(
                Modifier.padding(horizontal = 16.dp),
                selectedDate,
                mcDays,
                predictMcDay
            )

            if (!DateTimeUtils.isAfterToday(selectedDate) && runningMcDay == null && (lastMcDay == null || selectedDate > lastMcDay!!.endTime)) {
                AppButton(
                    text = Localization.menstruationStart,
                    onClick = { mcViewmodel.startNewMenstruationDay(selectedDate) }
                )
            }
            if (runningMcDay != null && selectedDate >= runningMcDay.startTime) {
                AppButton(
                    text = Localization.menstruationEnded,
                    onClick = { mcViewmodel.endMenstruationDay(selectedDate) }
                )
            }
        }
    }
}

@Composable
fun MenstrualCycleInfo(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    mcDays: List<MenstruationDay>,
    predictMcDay: MenstruationDay? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = DateTimeUtils.format(selectedDate, DateTimeUtils.FORMAT_ONLY_DATE),
                    style = MaterialTheme.typography.titleLarge
                )

                val mcDay = mcDays.getMcDay(selectedDate)
                val message = if (mcDay == null) {// 1.所选日期不存在月经记录
                    Localization.noMenstruation
                } else {// 2.所选日期存在月经记录
                    buildString {
                        val days = DateTimeUtils.getDurationDays(mcDay.startTime, selectedDate)
                        // 1.1.展示第几天
                        append(String.format(Localization.menstruationDays, days + 1))
                        // 1.2. 如果未结束，则展示预计结束时间
                        if (!mcDay.finish) {
                            appendLine()
                            // 预计{MENSTRUAL_DURATION}天内结束，如果超出，显示温馨提示
                            val endInDays = MENSTRUATION_DURATION - days
                            if (endInDays > 0) {
                                append(String.format(Localization.menstruationPredictEndIn, endInDays))
                            } else {
                                append(
                                    String.format(
                                        Localization.menstruationTips,
                                        MENSTRUATION_DURATION
                                    )
                                )
                            }
                        }
                    }
                }

                Text(text = message, style = MaterialTheme.typography.labelLarge)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) columnPredict@{
                Text(
                    text = Localization.menstruationPrediction,
                    style = MaterialTheme.typography.titleLarge
                )

                if (predictMcDay == null) return@columnPredict

                val lastMcDay = mcDays.last()
                val message =
                    if (selectedDate >= lastMcDay.endTime && selectedDate < predictMcDay.startTime) {
                        val days =
                            DateTimeUtils.getDurationDays(
                                DateTimeUtils.nowMillis(),
                                predictMcDay.startTime
                            )
                        String.format(
                            Localization.nextMenstruationInfo,
                            DateTimeUtils.format(predictMcDay.startTime, "yyyy.MM.dd"),
                            days
                        )
                    } else if (selectedDate in predictMcDay.startTime..predictMcDay.endTime) {
                        val days =
                            DateTimeUtils.getDurationDays(predictMcDay.startTime, selectedDate)
                        String.format(Localization.nextMenstruationDays, days + 1)
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
