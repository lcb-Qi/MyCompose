package com.lcb.one.util

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.MyApp

val PACKAGE_ME = BuildConfig.APPLICATION_ID

object DimenUtils {
    private val density = MyApp.getAppContext().resources.displayMetrics.density
    private val scaledDensity = MyApp.getAppContext().resources.displayMetrics.scaledDensity
    fun dp2px(dpValue: Int): Int {

        return (dpValue * density + 0.5f).toInt()
    }

    fun px2dp(pxValue: Int): Int {
        return (pxValue / density + 0.5f).toInt()
    }

    fun sp2px(spValue: Int): Float {
        return spValue * scaledDensity
    }
}

object ToastUtils {
    fun showToast(msg: String) {
        if (msg.isBlank()) return

        if (ThreadUtils.isOnMainThread()) {
            Toast.makeText(MyApp.getAppContext(), msg, Toast.LENGTH_SHORT).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(MyApp.getAppContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

object SharedPrefUtils {
    val defaultPreferences =
        PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext())

    val KEY_POEM_TOKEN = "poem_token"
    val KEY_LAST_POEM = "last_poem"
    val KEY_APP_DYNAMIC_COLOR = MyApp.getAppContext().getString(R.string.settings_dynamic_color_key)

    fun saveToken(token: String) {
        putString(KEY_POEM_TOKEN, token)
    }

    fun getToken(): String {
        return getString(KEY_POEM_TOKEN)
    }

    fun getPoem(): String {
        return getString(KEY_LAST_POEM)
    }

    fun savePoem(poem: String) {
        putString(KEY_LAST_POEM, poem)
    }

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