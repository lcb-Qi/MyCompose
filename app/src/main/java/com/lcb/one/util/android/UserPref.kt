package com.lcb.one.util.android

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.ui.MyApp

object UserPref {
    object Key {
        // global
        const val APP_THEME_SEED = "app_theme_seed"// int
        const val APP_DYNAMIC_COLOR = "app_dynamicColor"// boolean
        const val APP_ALOMED_MODE = "app_amoled_mode"// boolean
        const val USE_BUILTIN_BROWSER = "use_builtin_browser"// boolean
        const val DARK_MODE = "dark_mode"// int
        const val USER_SHORTCUTS = "user_shortcuts"// string set
        const val HOME_HEADER_BG = "home_header_bg"// uri

        // poem
        const val POEM_TOKEN = "poem_token"// string
        const val POEM_LAST = "last_poem"// json
        const val POEM_UPDATE_INTERVAL = "poem_update_duration"// int

        // clock screen
        const val CLOCK_TEXT_SIZE = "clock_text_size"// int
        const val CLOCK_DATE_POSITION = "date_position"// int
        const val CLOCK_DARK_THEME = "clock_dark_theme"// boolean

        // 首页
        const val PLAYER_REPEAT_MODE = "player_repeat_mode"
        const val PLAYER_IS_SHUFFLE = "player_is_SHUFFLE"
        const val PLAYER_LAST_MUSIC = "player_last_music"
    }

    val defaultPref: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(MyApp.get())

    fun getString(key: String, default: String = ""): String {
        if (key.isBlank()) return default
        return defaultPref.getString(key, default) ?: default
    }

    fun getStringSet(key: String, default: Set<String> = emptySet()): Set<String> {
        if (key.isBlank()) return default

        return defaultPref.getStringSet(key, default) ?: default
    }

    fun putStringSet(key: String, value: Set<String>) {
        if (key.isBlank()) return
        defaultPref.edit {
            putStringSet(key, value)
        }
    }

    fun putInt(key: String, value: Int) {
        if (key.isBlank()) return
        defaultPref.edit {
            putInt(key, value)
        }
    }

    fun getLong(key: String, default: Long = -1): Long {
        if (key.isBlank()) return default
        return defaultPref.getLong(key, default)
    }

    fun putLong(key: String, value: Long) {
        if (key.isBlank()) return
        defaultPref.edit {
            putLong(key, value)
        }
    }

    fun getInt(key: String, default: Int = -1): Int {
        if (key.isBlank()) return default
        return defaultPref.getInt(key, default)
    }

    fun putString(key: String, value: String) {
        if (key.isBlank()) return
        defaultPref.edit {
            putString(key, value)
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        if (key.isBlank()) return default
        return defaultPref.getBoolean(key, default)
    }

    fun putBoolean(key: String, value: Boolean) {
        if (key.isBlank()) return
        defaultPref.edit {
            putBoolean(key, value)
        }
    }
}