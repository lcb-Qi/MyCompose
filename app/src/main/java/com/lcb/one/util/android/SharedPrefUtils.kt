package com.lcb.one.util.android

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.ui.MyApp

object SharedPrefUtils {
    val defaultPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext())

    fun getString(key: String): String {
        if (key.isBlank()) return ""
        return defaultPreferences.getString(key, "") ?: ""
    }

    fun putInt(key: String, value: Int) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putInt(key, value)
        }
    }

    fun getLong(key: String): Long {
        if (key.isBlank()) return -1
        return defaultPreferences.getLong(key, -1)
    }

    fun putLong(key: String, value: Long) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putLong(key, value)
        }
    }

    fun getInt(key: String): Int {
        if (key.isBlank()) return -1
        return defaultPreferences.getInt(key, -1)
    }

    fun putString(key: String, value: String) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putString(key, value)
        }
    }

    fun getBoolean(key: String): Boolean {
        if (key.isBlank()) return false
        return defaultPreferences.getBoolean(key, false)
    }

    fun putBoolean(key: String, value: Boolean) {
        if (key.isBlank()) return
        defaultPreferences.edit {
            putBoolean(key, value)
        }
    }
}