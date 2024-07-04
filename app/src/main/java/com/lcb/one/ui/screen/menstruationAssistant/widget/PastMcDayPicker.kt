package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.widget.common.AppTextButton
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.toMillis
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PastMcDayPicker(
    show: Boolean,
    defaultSelectStartTime: Long? = null,
    defaultSelectEndTime: Long? = null,
    onCancel: () -> Unit = {},
    onDatePicked: ((LongRange) -> Unit)? = null
) {
    if (!show) return

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return !DateTimeUtils.isAfterToday(utcTimeMillis, ZoneId.of("UTC"))
        }
    }
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = defaultSelectStartTime,
        initialSelectedEndDateMillis = defaultSelectEndTime,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            AppTextButton(
                text = Localization.ok,
                onClick = {
                    val start = DateTimeUtils.toLocalDate(
                        pickerState.selectedStartDateMillis!!,
                        ZoneId.of("UTC")
                    )
                    val end = DateTimeUtils.toLocalDate(
                        pickerState.selectedEndDateMillis!!,
                        ZoneId.of("UTC")
                    )
                    onDatePicked?.invoke(start.toMillis()..end.toMillis())
                },
                enabled = pickerState.selectedStartDateMillis != null && pickerState.selectedEndDateMillis != null
            )
        },
        dismissButton = {
            AppTextButton(text = Localization.cancel, onClick = onCancel)
        },
        content = {
            val dateFormatter = remember { DatePickerDefaults.dateFormatter() }
            DateRangePicker(
                showModeToggle = false,
                state = pickerState,
                dateFormatter = dateFormatter,
                title = {
                    Text(
                        text = Localization.doImport,
                        modifier = Modifier.padding(
                            start = 24.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                headline = {
                    DateRangePickerDefaults.DateRangePickerHeadline(
                        selectedStartDateMillis = pickerState.selectedStartDateMillis,
                        selectedEndDateMillis = pickerState.selectedEndDateMillis,
                        displayMode = pickerState.displayMode,
                        dateFormatter,
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                    )
                }
            )
        }
    )
}