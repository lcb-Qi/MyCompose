package com.lcb.one.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppNavGraph
import com.lcb.one.ui.theme.AppTheme
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.settings.storage.disk.rememberBooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.rememberIntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.ui.widget.settings.ui.SettingSingleChoice
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsGroup
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSlider
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSwitch
import com.lcb.one.util.android.UserPrefManager
import com.ramcosta.composedestinations.annotation.ActivityDestination
import java.util.Locale

@ActivityDestination<AppNavGraph>
class ClockActivity : ComponentActivity() {
    companion object {
        private const val MAX_SIZE = 48/* sp */
        private const val MIN_SIZE = 6
        private const val DEFAULT_CLOCK_SIZE = 20
        private const val DEFAULT_DATE_SIZE = MIN_SIZE

        private const val DATE_POSITION_LEFT_BOTTOM = 0
        private const val DATE_POSITION_RIGHT_BOTTOM = 1
        private const val DATE_POSITION_CENTER_ABOVE = 2
        private const val DATE_POSITION_CENTER_BELOW = 3
    }

    data class RadioItem(val id: Int, val label: String)

    private val positions by lazy {
        listOf(
            RadioItem(DATE_POSITION_LEFT_BOTTOM, Localization.leftBottom),
            RadioItem(DATE_POSITION_RIGHT_BOTTOM, Localization.rightBottom),
            RadioItem(DATE_POSITION_CENTER_ABOVE, Localization.centerAbove),
            RadioItem(DATE_POSITION_CENTER_BELOW, Localization.centerBelow),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ClockScreen() }
        hideSystemBars()
    }

    @Composable
    fun ClockScreen() {
        var darkTheme by rememberBooleanPrefState(
            UserPrefManager.Key.CLOCK_DARK_THEME,
            ThemeManager.dynamicColor
        )

        var clockSize by rememberIntPrefState(
            UserPrefManager.Key.CLOCK_TEXT_SIZE,
            DEFAULT_CLOCK_SIZE
        )
        var datePosition by rememberIntPrefState(
            UserPrefManager.Key.CLOCK_DATE_POSITION,
            DATE_POSITION_LEFT_BOTTOM
        )

        AppTheme(darkTheme = darkTheme) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(modifier = Modifier.padding(24.dp)) {
                            SimpleSettingsGroup(title = Localization.settings) {
                                SimpleSettingsSwitch(
                                    checked = darkTheme,
                                    title = Localization.darkMode,
                                    summary = Localization.onlyThisPage,
                                    onCheckedChange = { darkTheme = it }
                                )

                                SimpleSettingsSlider(
                                    title = Localization.clockFontSize,
                                    summary = clockSize.toString(),
                                    value = clockSize.toFloat(),
                                    valueRange = MIN_SIZE.toFloat()..MAX_SIZE.toFloat(),
                                    onValueChange = {
                                        clockSize = it.toInt()
                                    }
                                )

                                val options = positions.map { it.label }
                                val values = positions.map { it.id }
                                SettingSingleChoice(
                                    title = Localization.positionOfDate,
                                    selectIndex = values.indexOf(datePosition).coerceAtLeast(0),
                                    options = options.toTypedArray(),
                                    onItemSelected = { datePosition = it }
                                )
                            }
                        }
                    }
                },
                content = { ClockView(clockSize, datePosition) }
            )
        }
    }

    @Composable
    fun ClockView(clockSize: Int, datePosition: Int) {
        val density = LocalDensity.current

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
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
                    setTypeface(Typeface.MONOSPACE, Typeface.BOLD)
                    gravity = Gravity.CENTER
                    builder?.invoke(this)
                }
            },
            update = { update?.invoke(it) }
        )
    }

    private fun hideSystemBars() {
        window.insetsController?.run {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsets.Type.systemBars())
        }
    }
}
