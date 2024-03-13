package com.lcb.one.util.android

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.ui.MyApp

object SharedPrefUtils {
    val defaultPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext())

    fun getString(key: String, default: String = ""): String {
        if (key.isBlank()) return default
        return defaultPreferences.getString(key, default) ?: default
    }

    fun putInt(key: String, value: Int) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putInt(key, value)
        }
    }

    fun getLong(key: String, default: Long = -1): Long {
        if (key.isBlank()) return default
        return defaultPreferences.getLong(key, default)
    }

    fun putLong(key: String, value: Long) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putLong(key, value)
        }
    }

    fun getInt(key: String, default: Int = -1): Int {
        if (key.isBlank()) return default
        return defaultPreferences.getInt(key, default)
    }

    fun putString(key: String, value: String) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putString(key, value)
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        if (key.isBlank()) return default
        return defaultPreferences.getBoolean(key, default)
    }

    fun putBoolean(key: String, value: Boolean) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putBoolean(key, value)
        }
    }
}