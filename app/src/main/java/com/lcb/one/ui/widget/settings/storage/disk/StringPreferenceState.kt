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
fun rememberStringPreferenceState(
    key: String,
    defaultValue: String? = null,
    preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current),
): StringPreferenceState {
    return remember {
        StringPreferenceState(
            key = key,
            preferences = preferences,
            defaultValue = defaultValue,
        )
    }
}

class StringPreferenceState(
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext()),
    val key: String,
    val defaultValue: String?,
) : SettingValueState<String?> {

    private var _value by mutableStateOf(preferences.getString(key, defaultValue))

    override var value: String?
        set(value) {
            _value = value
            preferences.edit { putString(key, value) }
        }
        get() = _value

    override fun reset() {
        value = defaultValue
    }
}
