package com.lcb.one.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lcb.one.R
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPreferenceState
import com.lcb.one.ui.widget.settings.storage.disk.IntPreferenceState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue

object AppSettings {
    var appTitle by mutableStateOf(MyApp.getAppContext().getString(R.string.app_name))
    
    var appDynamicColor by BooleanPreferenceState(
        key = MyApp.getAppContext().getString(R.string.settings_dynamic_color_key),
        defaultValue = true
    )

    var poemUpdateDuration by IntPreferenceState(
        key = MyApp.getAppContext().getString(R.string.settings_poem_update_duration_key),
        defaultValue = 15 * 60 * 1000
    )
}