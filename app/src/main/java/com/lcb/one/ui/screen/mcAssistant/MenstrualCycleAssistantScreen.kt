package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lcb.one.bean.McDay
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.navigateSingleTop
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.DateTimeUtils.toMillis
import com.lcb.one.viewmodel.MenstrualCycleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun MenstrualCycleAssistantScreen(navController: NavController) {
    val mcViewmodel = viewModel<MenstrualCycleViewModel>()
    val mcDays by mcViewmodel.getMcDays().collectAsState(emptyList())
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        ToolBar(title = "Menstrual Cycle Assistant(Beta)", actions = {
            IconButton(onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_HISTORY) }) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.List, contentDescription = "")
            }
        })
    }) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            val (calendarRef, messageRef, todayRef) = createRefs()
            var selectDay by remember { mutableLongStateOf(DateTimeUtils.nowMillis()) }
            Column(
                modifier = Modifier.constrainAs(calendarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Calendar(selectDay, mcDays) {
                    selectDay = it
                }

                Button(onClick = {
                    mcViewmodel.add(
                        McDay(
                            startTime = LocalDate.of(2024, 4, 6).toMillis(),
                            endTime = LocalDate.of(2024, 4, 7).toMillis()
                        )
                    )
                }) {
                    Text(text = "add")
                }

                Button(onClick = {
                    scope.launch(Dispatchers.IO) {
                        LLog.d("TAG", "getByTime: ${mcViewmodel.getByTime(LocalDate.now())}")
                    }
                }) {
                    Text(text = "getByTime")
                }
            }

            // FloatingActionButton(onClick = {
            //     mcViewmodel.add(
            //         McDay(
            //             startTime = LocalDate.now().toMillis(),
            //             endTime = LocalDate.now().plusDays(7).toMillis()
            //         )
            //     )
            // }) {
            //     Icon(imageVector = Icons.Rounded.LockReset, contentDescription = "")
            // }
        }
    }
}