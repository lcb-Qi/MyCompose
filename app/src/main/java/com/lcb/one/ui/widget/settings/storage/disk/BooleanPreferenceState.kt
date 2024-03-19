package com.lcb.one.ui.widget.settings.storage.disk

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.settings.storage.SettingValueState

@Composable
fun rememberBooleanPreferenceState(
    key: String,
    defaultValue: Boolean,
    preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current),
): BooleanPreferenceState {
    return remember {
        BooleanPreferenceState(
            preferences = preferences,
            key = key,
            defaultValue = defaultValue,
        )
    }
}

class BooleanPreferenceState(
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext()),
    val key: String,
    val defaultValue: Boolean = false,
) : SettingValueState<Boolean> {

    private var _value by mutableStateOf(preferences.getBoolean(key, defaultValue))

    override var value: Boolean
        set(value) {
            _value = value
            preferences.edit { putBoolean(key, value) }
        }
        get() = _value

    override fun reset() {
        value = defaultValue
    }
}
