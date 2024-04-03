package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenstrualCycleAssistantScreen() {
    rememberDatePickerState()
    Scaffold(topBar = { ToolBar(title = "Menstrual Cycle Assistant(Beta)") }) { paddingValues ->
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
                Calendar(selectDay) {
                    LLog.d("TAG", "MenstrualCycleAssistantScreen: Calendar dateChanged")
                    selectDay = it
                }
                Text(text = selectDay.debugAsDateTime())
            }

            // FloatingActionButton(onClick = { selectDay = DateTimeUtils.nowMillis() }) {
            //     Icon(imageVector = Icons.Rounded.LockReset, contentDescription = "")
            // }
        }
    }
}

private fun Long.debugAsDateTime(): String {
    val dateTime = DateTimeUtils.toLocalDateTime(this)
    val text = """
                timestamp ======== $this
                format ======== ${DateTimeUtils.format(this)}
                LocalDateTime ======== $dateTime
                year ======== ${dateTime.year}
                month ======== ${dateTime.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}
                dayOfWeek ======== ${
        dateTime.dayOfWeek.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )
    }
                dayOfMonth ======== ${dateTime.dayOfMonth}
                dayOfYear ======== ${dateTime.dayOfYear}
                isLeapYear ======== ${dateTime.toLocalDate().isLeapYear}
            """.trimIndent()
    return text
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun sss() {
    Column {
        DatePicker(state = rememberDatePickerState())
        DateRangePicker(state = rememberDateRangePickerState())
    }
}