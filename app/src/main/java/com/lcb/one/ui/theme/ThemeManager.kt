package com.lcb.one.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.UserPref
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

object ThemeManager {
    val seeds = defaultSeeds

    // 主题种子
    private var themeSeed by IntPrefState(
        UserPref.Key.APP_THEME_SEED,
        seeds.first().toArgb()
    )

    // 主题色
    var themeColor = Color(themeSeed)
        get() = Color(themeSeed)
        set(value) {
            field = value
            themeSeed = value.toArgb()
        }

    // 是否动态取色
    var dynamicColor by BooleanPrefState(UserPref.Key.APP_DYNAMIC_COLOR, true)

    // 是否启用纯白/纯黑背景
    var amoledMode by BooleanPrefState(UserPref.Key.APP_ALOMED_MODE, false)

    private const val DARK_MODE_AUTO = -1
    private const val DARK_MODE_LIGHT = 0
    private const val DARK_MODE_DARK = 1
    val darkModeValues: IntArray = intArrayOf(DARK_MODE_AUTO, DARK_MODE_LIGHT, DARK_MODE_DARK)
    var darkMode by IntPrefState(UserPref.Key.DARK_MODE, DARK_MODE_AUTO)

    @Composable
    fun calculateDarkTheme(): Boolean {
        val activity = LocalContext.current as? Activity

        checkNotNull(activity) { "current activity is null" }

        return if (darkMode == DARK_MODE_AUTO) {
            val systemInDarkTheme = isSystemInDarkTheme()
            AppUtils.lightStatusBars(activity, !systemInDarkTheme)

            systemInDarkTheme
        } else {
            AppUtils.lightStatusBars(activity, darkMode == DARK_MODE_LIGHT)

            darkMode == DARK_MODE_DARK
        }
    }
}

@Composable
fun AppTheme(
    darkTheme: Boolean = ThemeManager.calculateDarkTheme(),
    dynamicColor: Boolean = ThemeManager.dynamicColor,
    seedColor: Color = ThemeManager.themeColor,
    amoledMode: Boolean = ThemeManager.amoledMode,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> dynamicColorScheme(
            seedColor = seedColor,
            isDark = darkTheme,
            style = PaletteStyle.Vibrant,
        )
    }.let {
        if (amoledMode) {
            it.copy(
                background = if (darkTheme) Color.Black else Color.White,
                surface = if (darkTheme) Color.Black else Color.White,
            )
        } else {
            it
        }

    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = { Surface(modifier = Modifier.fillMaxSize(), content = { content() }) }
    )
}
