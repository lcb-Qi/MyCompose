package com.lcb.one.ui.widget.settings.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.lcb.one.util.android.SharedPrefUtils
import java.lang.IllegalArgumentException
import kotlin.reflect.KProperty

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> SettingValueState<T>.getValue(thisObj: Any?, property: KProperty<*>): T =
    value

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> SettingValueState<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T,
) {
    this.value = value
}

interface SettingValueState<T> {
    fun reset()

    var value: T
}

open class PreferenceSettingState<T>(
    private val key: String,
    private val defaultValue: T,
    private val preferences: SharedPreferences = SharedPrefUtils.defaultPreferences,
) : SettingValueState<T?> {

    private var _value: T? = preferences.get(key, defaultValue)

    override var value: T?
        set(value) {
            preferences.put(key, value)
        }
        get() = _value

    override fun reset() {
        value = defaultValue
    }
}

class BooleanPreference(
    key: String,
    defaultValue: Boolean,
) : PreferenceSettingState<Boolean>(key, defaultValue)


fun <T> SharedPreferences.put(key: String, value: T) {
    edit {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            // is Set<String> -> putStringSet(key, value)
            else -> throw IllegalArgumentException("")
        }
    }
}

fun <T> SharedPreferences.get(key: String, defaultValue: T?): T? {
    return when (defaultValue) {
        is Boolean -> getBoolean(key, defaultValue) as T?
        is String -> getString(key, defaultValue) as T?
        is Int -> getInt(key, defaultValue) as T?
        is Float -> getFloat(key, defaultValue) as T?
        is Long -> getLong(key, defaultValue) as T?
        else -> defaultValue
    }
}