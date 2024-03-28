package com.lcb.one.ui.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.FriendlyExitHandler
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.android.SharedPrefUtils
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    FriendlyExitHandler()
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        val anniversary = createRef()
        Column(
            modifier = modifier.constrainAs(anniversary) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PhotoFrame()
            DurationText()
        }

        val slogan = createRef()
        Text(
            text = "Code is technical debt",
            modifier = Modifier
                .constrainAs(slogan) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .alpha(0.5f),
            style = MaterialTheme.typography.bodySmall
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationText() {
    val keyStartTime = "start_time"
    val default = "2020-12-23 00:00:00"
    var long = SharedPrefUtils.getLong(keyStartTime)
    long = if (long > 0) {
        long
    } else {
        DateTimeUtils.toMillis(default)
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
        style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp),
        modifier = Modifier.clickable { showDialog = true }
    )

    val updater: (Long) -> Unit = {
        startTime = it
        SharedPrefUtils.putLong(keyStartTime, startTime)
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
    val keyPhotoUrl = "photo_url"
    var uri by remember { mutableStateOf(SharedPrefUtils.getString(keyPhotoUrl).toUri()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                uri = it
                SharedPrefUtils.putString(keyPhotoUrl, uri.toString())
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
            .heightIn(max = 200.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable { launcher.launch("image/*") }
    )
}