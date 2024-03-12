package com.lcb.one.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.lcb.one.R
import com.lcb.one.ui.widget.AppThemeSurface
import com.lcb.one.ui.widget.NoRippleInteractionSource
import com.lcb.one.ui.widget.settings.storage.disk.rememberPreferenceIntSettingState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.DimenUtils
import com.lcb.one.util.common.DateTimeUtils

class ClockActivity : ComponentActivity() {
    companion object {
        const val MAX_SIZE = 48
        const val MIN_SIZE = 12
        const val DEFAULT_SIZE = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ClockView() }
    }

    @Composable
    fun ClockView() {
        AppThemeSurface {
            var clockSize by
            rememberPreferenceIntSettingState(key = "clock_text_size", DEFAULT_SIZE)
            var showSetting by remember { mutableStateOf(false) }
            var onlyClock by remember { mutableStateOf(false) }

            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .clickable(
                    interactionSource = remember { NoRippleInteractionSource() },
                    indication = null
                ) {
                    onlyClock = !onlyClock
                }
            ) {
                val textClock = createRef()
                val clockColor = MaterialTheme.colorScheme.onBackground.toArgb()
                AndroidView(
                    modifier = Modifier
                        .constrainAs(textClock) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .fillMaxSize(),
                    factory = {
                        TextClock(it).apply {
                            format24Hour = "HH:mm:ss"
                            textSize = DimenUtils.sp2px(clockSize)
                            setTextColor(clockColor)
                            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            gravity = Gravity.CENTER
                        }
                    },
                    update = {
                        it.textSize = DimenUtils.sp2px(clockSize)
                    }
                )

                if (!onlyClock) {
                    val row = createRef()
                    Column(
                        modifier = Modifier.constrainAs(row) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { showSetting = true }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreHoriz,
                                contentDescription = "",
                            )
                        }
                    }
                }

                val date = createRef()
                Text(
                    modifier = Modifier.constrainAs(date) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    text = DateTimeUtils.format(DateTimeUtils.nowMillis(), "yyyy-MM-dd EE"),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }

            if (showSetting) {
                AlertDialog(
                    onDismissRequest = { showSetting = false },
                    confirmButton = {
                        TextButton(onClick = { showSetting = false }) {
                            Text(text = stringResource(R.string.ok))
                        }
                    },
                    text = {
                        val enablePlus = clockSize < MAX_SIZE
                        val enableMinus = clockSize > MIN_SIZE

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "字号 $clockSize")

                                IconButton(
                                    enabled = enableMinus,
                                    onClick = { clockSize-- }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = ""
                                    )
                                }

                                Slider(
                                    modifier = Modifier.width(120.dp),
                                    value = clockSize.toFloat(),
                                    onValueChange = { clockSize = it.toInt() },
                                    valueRange = MIN_SIZE.toFloat()..MAX_SIZE.toFloat(),
                                )

                                IconButton(
                                    enabled = enablePlus,
                                    onClick = { clockSize++ }) {
                                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
                                }
                            }
                        }
                    }
                )
            }

            hideSystemBars()
        }
    }

    private fun hideSystemBars() {
        window.insetsController?.run {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsets.Type.systemBars())
        }
    }
}
