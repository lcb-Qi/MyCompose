package com.lcb.one.ui.screen.menstruationAssistant

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.R
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.service.MenstruationAssistantQsService
import com.lcb.one.ui.Screen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.ui.screen.menstruationAssistant.repo.MenstruationViewModel
import com.lcb.one.ui.screen.menstruationAssistant.widget.Calendar
import com.lcb.one.ui.screen.menstruationAssistant.widget.CalendarColor
import com.lcb.one.ui.screen.menstruationAssistant.widget.PredictMessages
import com.lcb.one.ui.screen.menstruationAssistant.widget.MenstruationInfo
import com.lcb.one.ui.screen.menstruationAssistant.widget.rememberCalendarState
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.util.android.Res
import java.time.LocalDate


object MenstruationAssistantScreen : Screen {
    private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L// 一天的毫秒数
    private const val MENSTRUATION_INTERVAL = 28L// 两次月经间隔
    private const val MENSTRUATION_DURATION = 7L// 月经持续时间

    override val route: String
        get() = "MenstruationAssistant"

    override val label: String
        get() = Res.string(R.string.menstruation_assistant)

    @Composable
    override fun Content(args: Bundle?) {
        val navController = LocalNav.current!!

        val mcViewmodel = viewModel<MenstruationViewModel>()
        val mcDays by mcViewmodel.all.collectAsState(emptyList())
        val runningMcDay by remember { derivedStateOf { mcDays.firstOrNull { !it.finish } } }
        val lastMcDay by remember { derivedStateOf { mcDays.lastOrNull() } }
        val predictMcDay by remember {
            derivedStateOf {
                lastMcDay?.let {
                    it.copy(
                        finish = true,
                        startTime = it.startTime + MENSTRUATION_INTERVAL * ONE_DAY_MILLIS,
                        endTime = it.startTime + (MENSTRUATION_INTERVAL + MENSTRUATION_DURATION - 1) * ONE_DAY_MILLIS
                    )
                }
            }
        }
        val calendarState = rememberCalendarState(
            primaryRange = mcDays.map { it.toRange() },
            secondaryRange = predictMcDay?.toRange()
        )
        LaunchedEffect(mcDays) {
            calendarState.updatePrimaryRange(mcDays.map { it.toRange() })
            calendarState.updateSecondaryRange(predictMcDay?.toRange())
        }

        Scaffold(
            topBar = {
                ToolBar(
                    title = label,
                    actions = {
                        AppIconButton(
                            icon = Icons.Rounded.History,
                            onClick = { navController.launchSingleTop(MenstruationHistoryScreen) }
                        )
                    }
                )
            },
            floatingActionButton = {
                val showFab by remember {
                    derivedStateOf { !DateTimeUtils.isToday(calendarState.selectedMillis) }
                }
                if (showFab) {
                    FloatingActionButton(onClick = { calendarState.updateSelectedDate(LocalDate.now()) }) {
                        Icon(Icons.Rounded.Today, null)
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Calendar(state = calendarState)

                LegendTips(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    color = calendarState.colors
                )

                MenstruationInfo(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    selectedDate = calendarState.selectedMillis,
                    mcDays = mcDays
                )

                PredictMessages(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    selectedDate = calendarState.selectedMillis,
                    predictMcDay = predictMcDay
                )


                val showStart by remember {
                    derivedStateOf {
                        // 1.所选日期在今天之前（包含）
                        // 2.没有未结束的经期
                        // 3.所选日期在最后一次经期之后或没有经期记录
                        !DateTimeUtils.isAfterToday(calendarState.selectedMillis)
                                && runningMcDay == null
                                && (lastMcDay == null || calendarState.selectedMillis > lastMcDay!!.endTime)
                    }
                }

                if (showStart) {
                    AppButton(
                        text = stringResource(R.string.menstruation_start),
                        onClick = { mcViewmodel.startNewMenstruationDay(calendarState.selectedMillis) }
                    )
                }

                val showEnd by remember {
                    derivedStateOf {
                        // 1.存在未结束的经期
                        // 2.所选日期介于开始日期和今天之间（闭区间）
                        // 对于未结束的，toRange会自动转换结束日期为今天，这里直接调用即可
                        runningMcDay != null
                                && calendarState.selectedMillis in runningMcDay!!.toRange()
                    }
                }
                if (showEnd) {
                    AppButton(
                        text = stringResource(R.string.menstruation_ended),
                        onClick = { mcViewmodel.endMenstruationDay(calendarState.selectedMillis) }
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            MenstruationAssistantQsService.tryAddQs()
        }
    }

    @Composable
    private fun LegendTips(modifier: Modifier = Modifier, color: CalendarColor) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = ColorPainter(color.primary),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                )

                Text(text = stringResource(R.string.menstruation))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    ColorPainter(color.secondary),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                )

                Text(text = stringResource(R.string.predict_menstruation))
            }
        }
    }
}