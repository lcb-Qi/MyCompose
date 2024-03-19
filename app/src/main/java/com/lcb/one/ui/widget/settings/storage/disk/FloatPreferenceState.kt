package com.lcb.one.ui.widget.settings.storage.disk

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.settings.storage.SettingValueState

@Composable
fun rememberFloatPreferenceState(
    key: String,
    defaultValue: Float = 0f,
    preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current),
): FloatPreferenceState {
    return remember {
        FloatPreferenceState(
            key = key,
            preferences = preferences,
            defaultValue = defaultValue,
        )
    }
}

class FloatPreferenceState(
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext()),
    val key: String,
    val defaultValue: Float = 0f,
) : SettingValueState<Float> {

    private var _value by mutableFloatStateOf(preferences.getFloat(key, defaultValue))

    override var value: Float
        set(value) {
            _value = value
            preferences.edit { putFloat(key, value) }
        }
        get() = _value

    override fun reset() {
        value = defaultValue
    }
}
