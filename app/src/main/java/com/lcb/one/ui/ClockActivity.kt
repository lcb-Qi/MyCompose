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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.lcb.one.R
import com.lcb.one.ui.widget.AppThemeSurface
import com.lcb.one.ui.widget.NoRippleInteractionSource
import com.lcb.one.ui.widget.settings.storage.disk.rememberBooleanPreferenceState
import com.lcb.one.ui.widget.settings.storage.disk.rememberIntPreferenceState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.SharedPrefUtils
import java.util.Locale

class ClockActivity : ComponentActivity() {
    companion object {
        private const val MAX_SIZE = 48/* sp */
        private const val MIN_SIZE = 6
        private const val DEFAULT_SIZE = 20
        private const val DEFAULT_DATE_SIZE = MIN_SIZE

        private const val DATE_POSITION_LEFT_BOTTOM = 0
        private const val DATE_POSITION_RIGHT_BOTTOM = 1
        private const val DATE_POSITION_CENTER_ABOVE = 2
        private const val DATE_POSITION_CENTER_BELOW = 3

        private const val KEY_CLOCK_SIZE = "clock_text_size"
        private const val KEY_DATE_POSITION = "date_position"
        private const val KEY_CLOCK_DARK_THEME = "clock_dark_theme"
    }

    data class RadioItem(val id: Int, val label: String)

    private val positions by lazy {
        listOf(
            RadioItem(DATE_POSITION_LEFT_BOTTOM, getString(R.string.left_bottom)),
            RadioItem(DATE_POSITION_RIGHT_BOTTOM, getString(R.string.right_bottom)),
            RadioItem(DATE_POSITION_CENTER_ABOVE, getString(R.string.center_above)),
            RadioItem(DATE_POSITION_CENTER_BELOW, getString(R.string.center_below)),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ClockView() }
        hideSystemBars()
    }

    private fun getClockSize(): Int {
        return SharedPrefUtils.getInt(KEY_CLOCK_SIZE, MIN_SIZE)
    }

    private fun getDatePosition(): Int {
        return SharedPrefUtils.getInt(KEY_DATE_POSITION, DATE_POSITION_LEFT_BOTTOM)
    }

    @Composable
    fun ClockView() {
        var darkTheme by rememberBooleanPreferenceState(
            KEY_CLOCK_DARK_THEME,
            AppSettings.appDynamicColor
        )
        AppThemeSurface(darkTheme = darkTheme) {
            var clockSize by
            rememberIntPreferenceState(KEY_CLOCK_SIZE, DEFAULT_SIZE)
            var datePosition by rememberIntPreferenceState(
                KEY_DATE_POSITION,
                DATE_POSITION_LEFT_BOTTOM
            )
            val density = LocalDensity.current
            var showSetting by remember { mutableStateOf(false) }
            var onlyClock by remember { mutableStateOf(true) }

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

                TextClock(
                    modifier = Modifier.constrainAs(textClock) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    builder = {
                        it.format12Hour = "hh:mm:ss"
                        it.format24Hour = "HH:mm:ss"
                        it.textSize = density.run { clockSize.sp.toPx() }
                        it.setTextColor(clockColor)
                    },
                    update = {
                        it.textSize = density.run { clockSize.sp.toPx() }
                        it.setTextColor(clockColor)
                    }
                )

                val row = createRef()
                if (!onlyClock) {
                    Row(
                        modifier = Modifier.constrainAs(row) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconToggleButton(
                            checked = darkTheme,
                            onCheckedChange = { darkTheme = it }) {
                            Icon(
                                imageVector = Icons.Rounded.DarkMode,
                                contentDescription = "",
                            )
                        }

                        IconButton(onClick = { showSetting = true }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreHoriz,
                                contentDescription = "",
                            )
                        }
                    }
                }

                val date = createRef()
                TextClock(
                    modifier = Modifier.constrainAs(date) {
                        updateDatePosition(datePosition, textClock)
                    },
                    builder = {
                        val locale = Locale.getDefault()
                        val format = if (locale.language == Locale.CHINESE.language) {
                            "M月d日 EE"
                        } else if (locale == Locale.UK) {
                            "EE d MMMM"
                        } else if (locale == Locale.US) {
                            "EE,MMM d"
                        } else {
                            "M-d EE"
                        }
                        it.format12Hour = "$format aa"
                        it.format24Hour = format
                        it.textSize = density.run { DEFAULT_DATE_SIZE.sp.toPx() }
                        it.setTextColor(clockColor)
                    },
                    update = { it.setTextColor(clockColor) }
                )
            }

            ClockSettingDialog(
                show = showSetting,
                onDismiss = { showSetting = false },
                onTextSizeChanged = { clockSize = it },
                onDatePositionChange = { datePosition = it }
            )
        }
    }

    private fun ConstrainScope.updateDatePosition(
        datePosition: Int,
        textClock: ConstrainedLayoutReference
    ) {
        when (datePosition) {
            DATE_POSITION_LEFT_BOTTOM -> {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }

            DATE_POSITION_RIGHT_BOTTOM -> {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }

            DATE_POSITION_CENTER_ABOVE -> {
                start.linkTo(textClock.start)
                end.linkTo(textClock.end)
                bottom.linkTo(textClock.top)
            }

            DATE_POSITION_CENTER_BELOW -> {
                start.linkTo(textClock.start)
                end.linkTo(textClock.end)
                top.linkTo(textClock.bottom)
            }

            else -> {}
        }
    }

    @Composable
    fun TextClock(
        modifier: Modifier = Modifier,
        builder: ((TextClock) -> Unit)? = null,
        update: ((TextClock) -> Unit)? = null
    ) {
        AndroidView(
            modifier = modifier,
            factory = {
                TextClock(it).apply {
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    gravity = Gravity.CENTER
                    builder?.invoke(this)
                }
            },
            update = { update?.invoke(it) }
        )
    }

    @Composable
    fun ClockSettingDialog(
        show: Boolean,
        onDismiss: () -> Unit,
        onTextSizeChanged: (Int) -> Unit,
        onDatePositionChange: (Int) -> Unit,
    ) {
        if (!show) return

        var clockSize by remember { mutableIntStateOf(getClockSize()) }
        var position by remember { mutableIntStateOf(getDatePosition()) }
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            title = { Text(text = stringResource(R.string.settings)) },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            text = {
                val enablePlus = clockSize < MAX_SIZE
                val enableMinus = clockSize > MIN_SIZE

                Column(Modifier.width(420.dp)) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "字号 $clockSize", modifier = Modifier.wrapContentWidth())

                        IconButton(
                            enabled = enableMinus,
                            onClick = {
                                clockSize--
                                onTextSizeChanged(clockSize)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Remove,
                                contentDescription = ""
                            )
                        }

                        Slider(
                            modifier = Modifier.weight(1f),
                            value = clockSize.toFloat(),
                            onValueChange = {
                                clockSize = it.toInt()
                                onTextSizeChanged(clockSize)
                            },
                            valueRange = MIN_SIZE.toFloat()..MAX_SIZE.toFloat(),
                        )

                        IconButton(
                            enabled = enablePlus,
                            onClick = {
                                clockSize++
                                onTextSizeChanged(clockSize)
                            }
                        ) {
                            Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.date_position))
                        positions.forEach {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = position == it.id,
                                    onClick = {
                                        position = it.id
                                        onDatePositionChange(position)
                                    }
                                )
                                Text(text = it.label)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun hideSystemBars() {
        window.insetsController?.run {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsets.Type.systemBars())
        }
    }
}
