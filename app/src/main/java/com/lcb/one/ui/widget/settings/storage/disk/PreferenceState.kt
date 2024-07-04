package com.lcb.one.ui.widget.settings.storage.disk

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import com.lcb.one.ui.widget.settings.storage.SettingValueState
import com.lcb.one.util.android.UserPref
import java.lang.IllegalArgumentException

abstract class PreferenceState<T>(
    private val key: String,
    private val defaultValue: T,
    private val preferences: SharedPreferences = UserPref.defaultPref,
) : SettingValueState<T> {

    private var _value: T by mutableStateOf(preferences.get(key, defaultValue))

    override var value: T
        set(value) {
            _value = value
            preferences.put(key, value)
        }
        get() = _value
}

fun <T> SharedPreferences.put(key: String, value: T) {
    edit {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> throw IllegalArgumentException("unsupported type, use [Boolean|String|Int|Float|Long].")
        }
    }
}

fun <T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when (defaultValue) {
        is Boolean -> getBoolean(key, defaultValue) as T
        is String -> getString(key, defaultValue) as T
        is Int -> getInt(key, defaultValue) as T
        is Float -> getFloat(key, defaultValue) as T
        is Long -> getLong(key, defaultValue) as T
        else -> defaultValue
    }
}