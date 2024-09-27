package com.lcb.one.ui.screen.clock

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ConstraintSetScope
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.prefs.UserPrefs
import com.lcb.one.ui.Screen
import com.lcb.one.ui.theme.AppThemeSurface
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.prefs.getValue
import com.lcb.one.prefs.rememberPrefState
import com.lcb.one.prefs.setValue
import com.lcb.weight.settings.SettingsDefaults
import com.lcb.weight.settings.SettingsDropdownMenu
import com.lcb.weight.settings.SettingsSlider
import com.lcb.weight.settings.SettingsSwitch
import com.lcb.one.util.platform.Res
import com.lcb.weight.settings.SettingsCategory
import java.util.Locale

object ClockScreen : Screen() {
    override val label: String = Res.string(R.string.clock_screen)

    private fun Activity.hideSystemBars() {
        window.insetsController?.run {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsets.Type.systemBars())
        }
    }

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        val activity = LocalContext.current as Activity

        DisposableEffect(Unit) {
            val originalOrientation = activity.requestedOrientation
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            activity.hideSystemBars()
            onDispose {
                activity.requestedOrientation = originalOrientation
            }
        }

        var darkTheme by rememberPrefState(UserPrefs.Key.dynamicColor, ThemeManager.dynamicColor)
        var clockSizeScale by rememberPrefState(UserPrefs.Key.clockSize, 1.0f)
        var layout by rememberPrefState(UserPrefs.Key.clockLayout, LAYOUT_DEFAULT)

        AppThemeSurface(darkTheme = darkTheme) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(modifier = Modifier.padding(24.dp)) {
                            SettingsCategory(
                                title = stringResource(R.string.settings),
                                color = DrawerDefaults.modalContainerColor
                            ) {
                                val colors =
                                    SettingsDefaults.colorsOnContainer(DrawerDefaults.modalContainerColor)
                                SettingsSwitch(
                                    colors = colors,
                                    checked = darkTheme,
                                    title = stringResource(R.string.dark_mode),
                                    summary = stringResource(R.string.effective_this_page),
                                    onCheckedChange = { darkTheme = it }
                                )

                                SettingsSlider(
                                    colors = colors,
                                    title = stringResource(R.string.font_zoom),
                                    summary = "%.2f".format(clockSizeScale),
                                    value = clockSizeScale,
                                    valueRange = MIN_SCALED..MAX_SCALED,
                                    onValueChange = { clockSizeScale = it }
                                )

                                val options =
                                    layouts.map { "${Res.string(R.string.layout)} $it" }
                                SettingsDropdownMenu(
                                    colors = colors,
                                    title = stringResource(R.string.layout),
                                    selectIndex = layouts.indexOf(layout).coerceAtLeast(0),
                                    options = options.toTypedArray(),
                                    onItemSelected = { layout = layouts[it] }
                                )
                            }
                        }
                    }
                },
                content = { ClockDateLayout(clockSizeScale, layout) }
            )
        }
    }

    private const val MAX_SCALED = 3.0f
    private const val MIN_SCALED = 1.0f
    private const val DEFAULT_CLOCK_SIZE = 48
    private const val DEFAULT_DATE_SIZE = 16

    private const val LAYOUT_DEFAULT = 1
    private const val LAYOUT_1 = 2
    private val layouts = arrayOf(LAYOUT_DEFAULT, LAYOUT_1)

    @Composable
    private fun ClockDateLayout(scale: Float, layout: Int) {
        val backgroundColor = MaterialTheme.colorScheme.background
        ConstraintLayout(
            animateChanges = true,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            constraintSet = createColum {
                val refs = arrayOf(createRefFor("clock"), createRefFor("date"))
                if (layout == LAYOUT_1) {
                    refs.reversedArray()
                } else {
                    refs
                }
            }
        ) {
            val clockColor = MaterialTheme.colorScheme.contentColorFor(backgroundColor).toArgb()

            TextClock(
                modifier = Modifier.layoutId("clock"),
                builder = {
                    it.format12Hour = "hh:mm:ss"
                    it.format24Hour = "HH:mm:ss"
                    it.textSize = scale * DEFAULT_CLOCK_SIZE
                    it.setTextColor(clockColor)
                },
                update = {
                    it.textSize = scale * DEFAULT_CLOCK_SIZE
                    it.setTextColor(clockColor)
                }
            )

            TextClock(
                modifier = Modifier.layoutId("date"),
                builder = {
                    val format = getDateFormat()
                    it.format12Hour = "$format aa"
                    it.format24Hour = format
                    it.textSize = DEFAULT_DATE_SIZE * scale
                    it.setTextColor(clockColor)
                },
                update = {
                    it.textSize = DEFAULT_DATE_SIZE * scale
                    it.setTextColor(clockColor)
                }
            )
        }
    }

    private fun createColum(createRefs: ConstraintSetScope.() -> Array<ConstrainedLayoutReference>): ConstraintSet =
        ConstraintSet {
            val refs = createRefs()
            createVerticalChain(*refs, chainStyle = ChainStyle.Packed).apply {
                constrain(this) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            }

            refs.forEach {
                constrain(it) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            }
        }

    private fun getDateFormat(): String {
        val locale = Locale.getDefault()
        return if (locale.language == Locale.CHINESE.language) {
            "M月d日 EE"
        } else if (locale == Locale.UK) {
            "EE d MMMM"
        } else if (locale == Locale.US) {
            "EE,MMM d"
        } else {
            "M-d EE"
        }
    }
}