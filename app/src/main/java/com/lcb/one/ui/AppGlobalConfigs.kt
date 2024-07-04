package com.lcb.one.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lcb.one.BuildConfig
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.UserPref

object AppGlobalConfigs {
    const val COUNT_TO_ENABLE_DEV_MODE = 10
    var appDevMode by BooleanPrefState(UserPref.Key.APP_DEV_MODE, BuildConfig.DEBUG)
    var useBuiltinBrowser by BooleanPrefState(UserPref.Key.USE_BUILTIN_BROWSER, false)

    var poemUpdateInterval by IntPrefState(
        UserPref.Key.POEM_UPDATE_INTERVAL,
        24 * 60 * 60 * 1000
    )

    var assertNetwork by mutableStateOf(false)
}