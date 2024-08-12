package com.lcb.one.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.lcb.one.ui.widget.settings.storage.DataStoreState
import com.lcb.one.prefs.UserPrefs.Key
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.AppUtils

object ThemeManager {
    val seeds = defaultSeeds

    // 主题种子
    private var seed by DataStoreState(Key.seedColor, seeds.first().toArgb())

    // 主题色
    var themeColor = Color(seed)
        get() = Color(seed)
        set(value) {
            field = value
            seed = value.toArgb()
        }

    // 是否动态取色
    var dynamicColor by DataStoreState(Key.dynamicColor, true)

    // 是否启用纯白/纯黑背景
    var amoledMode by DataStoreState(Key.amoledMode, false)

    private const val DARK_MODE_AUTO = -1
    private const val DARK_MODE_LIGHT = 0
    private const val DARK_MODE_DARK = 1
    val darkModeValues: IntArray = intArrayOf(DARK_MODE_AUTO, DARK_MODE_LIGHT, DARK_MODE_DARK)
    var darkMode by DataStoreState(Key.darkMode, DARK_MODE_AUTO)

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