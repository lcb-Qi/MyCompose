package com.lcb.one.ui.widget.settings.storage.disk

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.ui.widget.settings.storage.SettingValueState

@Composable
fun rememberIntPreferenceState(
    key: String,
    defaultValue: Int = 0,
    preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current),
): IntPreferenceState {
    return remember {
        IntPreferenceState(
            key = key,
            preferences = preferences,
            defaultValue = defaultValue,
        )
    }
}

class IntPreferenceState(
    private val preferences: SharedPreferences,
    val key: String,
    val defaultValue: Int = 0,
) : SettingValueState<Int> {

    private var _value by mutableIntStateOf(preferences.getInt(key, defaultValue))

    override var value: Int
        set(value) {
            _value = value
            preferences.edit { putInt(key, value) }
        }
        get() = _value

    override fun reset() {
        value = defaultValue
    }
}
