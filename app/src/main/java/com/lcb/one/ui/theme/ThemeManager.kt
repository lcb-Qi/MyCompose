package com.lcb.one.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lcb.one.prefs.PrefState
import com.lcb.one.prefs.UserPrefs.Key
import com.lcb.one.prefs.getValue
import com.lcb.one.prefs.setValue
import com.lcb.one.util.platform.AppUtils

object ThemeManager {
    // 是否动态取色
    var dynamicColor by PrefState(Key.dynamicColor, true)

    // 是否启用纯白/纯黑背景
    var amoled by PrefState(Key.amoled, false)

    private const val UI_MODE_AUTO = -1
    private const val UI_MODE_LIGHT = 0
    private const val UI_MODE_DARK = 1
    val uiModes: IntArray = intArrayOf(UI_MODE_AUTO, UI_MODE_LIGHT, UI_MODE_DARK)
    var uiMode by PrefState(Key.uiMode, UI_MODE_AUTO)

    @Composable
    fun calculateDarkTheme(): Boolean {
        val activity = LocalContext.current as? Activity

        checkNotNull(activity) { "current activity is null" }

        return if (uiMode == UI_MODE_AUTO) {
            val systemInDarkTheme = isSystemInDarkTheme()
            AppUtils.lightStatusBars(activity, !systemInDarkTheme)

            systemInDarkTheme
        } else {
            AppUtils.lightStatusBars(activity, uiMode == UI_MODE_LIGHT)

            uiMode == UI_MODE_DARK
        }
    }
}