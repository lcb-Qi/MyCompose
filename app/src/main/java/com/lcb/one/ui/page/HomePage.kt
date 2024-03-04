package com.lcb.one.ui.page

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.theme.labelLarge
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.android.SharedPrefUtils
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

@Composable
@Preview(showSystemUi = false)
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PhotoFrame()
        DurationText()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationText() {
    var long = SharedPrefUtils.getLong("start_time")
    long = if (long > 0) {
        long
    } else {
        DateTimeUtils.toMillis("2020-12-23 00:00:00")
    }
    var startTime by remember { mutableLongStateOf(long) }
    var durationText by remember {
        mutableStateOf(DateTimeUtils.friendlyDuration(startTime))
    }

    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(utcTimeMillis), ZoneId.systemDefault())
            return dateTime.toLocalDate() <= LocalDate.now()
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year
        }
    })

    Text(
        text = durationText,
        style = labelLarge().copy(fontSize = 18.sp),
        modifier = Modifier.clickable { showDialog = true }
    )

    val updater: (Long) -> Unit = {
        startTime = it
        SharedPrefUtils.putLong("start_time", startTime)
        durationText = DateTimeUtils.friendlyDuration(it)
    }

    LaunchedEffect(key1 = "updater", block = {
        while (true) {
            updater(startTime)
            delay(1000)
        }
    })

    if (showDialog) {

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        datePickerState.selectedDateMillis?.let {
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = it
                            calendar.set(Calendar.HOUR_OF_DAY, 0)
                            calendar.set(Calendar.MINUTE, 0)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            updater(calendar.timeInMillis)
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun PhotoFrame() {
    var uri by remember { mutableStateOf(SharedPrefUtils.getString("image_url").toUri()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                uri = it
                SharedPrefUtils.putString("image_url", uri.toString())
                MyApp.getAppContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        })

    AsyncImage(
        model = uri,
        contentDescription = "",
        Modifier
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { launcher.launch("image/*") }
    )
}