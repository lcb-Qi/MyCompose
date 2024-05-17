package com.lcb.one.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue

object AppGlobalConfigs {
    const val COUNT_TO_ENABLE_DEV_MODE = 10
    var appDevMode by BooleanPrefState(
        key = MyApp.getAppContext().getString(R.string.settings_dev_mode_key),
        default = BuildConfig.DEBUG
    )

    var poemUpdateInterval by IntPrefState(
        key = MyApp.getAppContext().getString(R.string.settings_poem_update_interval_key),
        default = 24 * 60 * 60 * 1000
    )

    var assertNetwork by mutableStateOf(false)
}