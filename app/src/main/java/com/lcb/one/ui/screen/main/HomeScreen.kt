package com.lcb.one.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.localization.Localization
import com.lcb.one.ui.screen.main.widget.EventCard
import com.lcb.one.ui.widget.common.AppTextButton
import com.lcb.one.ui.widget.settings.storage.disk.rememberIntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingSingleChoice
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.util.android.UserPref
import com.lcb.one.util.common.DateTimeUtils
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { LoveCard() }
        item { OffWorkCard() }
        item { PaydayCard() }
    }
}

@Composable
private fun LoveCard() {
    val duration = remember {
        val startTime = DateTimeUtils.toMillis("2020-12-23 00:00:00")
        val days = DateTimeUtils.getDurationDays(startTime, DateTimeUtils.nowMillis())

        "$days ${Localization.days}"
    }

    EventCard(
        modifier = Modifier.fillMaxWidth(),
        title = Localization.fallInLove,
        content = duration,
        icon = R.drawable.icon_love
    )
}

@Composable
private fun OffWorkCard() {
    val startTime = LocalTime.of(9, 0)
    val endTime = LocalTime.of(18, 0)

    var title by remember { mutableStateOf(Localization.offWorkCountdown) }
    var content by remember { mutableStateOf("") }
    var icon by remember { mutableIntStateOf(R.mipmap.onwork) }

    val updater = {
        val nowTime = LocalTime.now()
        val dayOfWeek = LocalDate.now().dayOfWeek

        val isWorkday = (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)
        if (isWorkday && nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
            val duration = Duration.between(nowTime, endTime)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            val seconds = duration.seconds % 60

            title = Localization.offWorkCountdown
            content = String.format(Localization.friendDurationNoDays, hours, minutes, seconds)
            icon = R.mipmap.onwork
        } else {
            title = Localization.breakTime
            content = ""
            icon = R.mipmap.offwork
        }
    }

    EventCard(
        modifier = Modifier.fillMaxWidth(),
        title = title,
        content = content,
        icon = icon
    )

    LaunchedEffect(updater) {
        while (true) {
            updater()
            delay(1000)
        }
    }
}

@Composable
fun PaydayCard() {
    var showSettings by remember { mutableStateOf(false) }
    var targetDay by rememberIntPrefState(UserPref.Key.PAYDAY, 1)
    val today = LocalDate.now()

    var targetDate = today.withDayOfMonth(targetDay)
    if (today.isAfter(targetDate)) {
        targetDate = targetDate.plusMonths(1)
    }

    val days = Duration.between(today.atStartOfDay(), targetDate.atStartOfDay()).toDays()

    EventCard(
        modifier = Modifier.fillMaxWidth(),
        title = Localization.paydayCountdown,
        content = "$days ${Localization.days}",
        icon = R.drawable.icon_rmb,
        onClick = { showSettings = true }
    )

    if (showSettings) {
        val values = (1..20).toList()
        val options = values.map { it.toString() }
        var selectIndex by remember { mutableIntStateOf(values.indexOf(targetDay)) }
        AlertDialog(
            title = { Text(text = Localization.settings) },
            onDismissRequest = { showSettings = false },
            confirmButton = {
                AppTextButton(
                    text = Localization.ok,
                    onClick = {
                        targetDay = values[selectIndex]
                        showSettings = false
                    }
                )
            },
            dismissButton = {
                AppTextButton(
                    text = Localization.cancel,
                    onClick = { showSettings = false }
                )
            },
            text = {
                ProvideSettingsItemColor(SettingsDefaults.colorOnContainer(AlertDialogDefaults.containerColor)) {
                    SettingSingleChoice(
                        selectIndex = selectIndex,
                        title = Localization.payday,
                        options = options.toTypedArray(),
                        onItemSelected = { selectIndex = it }
                    )
                }
            }
        )
    }
}