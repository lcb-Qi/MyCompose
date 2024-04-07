package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.viewmodel.MenstrualCycleViewModel

@Composable
fun MenstrualCycleHistoryScreen() {
    val mcViewmodel = viewModel<MenstrualCycleViewModel>()
    Scaffold(topBar = { ToolBar(title = "MenstrualCycle History(beta)") }) { paddingValues ->
        val mcDays by mcViewmodel.getMcDays().collectAsState(emptyList())
        LazyColumn(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            LLog.d("TAG", "MenstrualCycleHistoryScreen: $mcDays")
            items(count = mcDays.size, key = { mcDays[it].startTime ?: it }) {
                val mcDay = mcDays[it]
                if (mcDay.startTime != null && mcDay.endTime != null) {
                    val startTime =
                        DateTimeUtils.format(mcDay.startTime, DateTimeUtils.FORMAT_ONLY_DATE)
                    val endTime =
                        DateTimeUtils.format(mcDay.endTime, DateTimeUtils.FORMAT_ONLY_DATE)
                    Text(text = "$startTime - $endTime")
                }
            }
        }
    }
}