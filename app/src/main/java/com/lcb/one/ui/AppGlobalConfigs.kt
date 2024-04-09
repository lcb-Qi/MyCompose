package com.lcb.one.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lcb.one.R
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPreferenceState
import com.lcb.one.ui.widget.settings.storage.disk.IntPreferenceState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue

object AppGlobalConfigs {
    var appDynamicColor by BooleanPreferenceState(
        key = MyApp.getAppContext().getString(R.string.settings_dynamic_color_key),
        defaultValue = true
    )

    var poemUpdateDuration by IntPreferenceState(
        key = MyApp.getAppContext().getString(R.string.settings_poem_update_duration_key),
        defaultValue = 24 * 60 * 60 * 1000
    )

    var assertNetwork by mutableStateOf(false)
}