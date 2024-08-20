package com.lcb.one.prefs

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.lcb.one.prefs.DateStores.get
import com.lcb.one.prefs.DateStores.getBlocking
import com.lcb.one.prefs.DateStores.put
import com.lcb.one.prefs.DateStores.putBlocking
import com.lcb.one.ui.widget.settings.storage.PrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue

object UserPrefs {
    object Key {
        val useBuiltInBrowser: Preferences.Key<Boolean>
            get() = booleanPreferencesKey("use_built_in_browser")

        val poemUpdateInterval: Preferences.Key<Int>
            get() = intPreferencesKey("poem_update_interval")

        val dynamicColor: Preferences.Key<Boolean>
            get() = booleanPreferencesKey("dynamic_color")

        val amoledMode: Preferences.Key<Boolean>
            get() = booleanPreferencesKey("amoled_mode")

        val uiMode: Preferences.Key<Int>
            get() = intPreferencesKey("ui_mode")

        val seedColor: Preferences.Key<Int>
            get() = intPreferencesKey("seed_color")

        val clockSize: Preferences.Key<Float>
            get() = floatPreferencesKey("clock_size")

        val clockLayout: Preferences.Key<Int>
            get() = intPreferencesKey("clock_layout")

        val shortcuts: Preferences.Key<Set<String>>
            get() = stringSetPreferencesKey("shortcuts")

        val homeHeader: Preferences.Key<String>
            get() = stringPreferencesKey("homeHeader")

        val token: Preferences.Key<String>
            get() = stringPreferencesKey("token")

        val lastPoem: Preferences.Key<String>
            get() = stringPreferencesKey("last_poem")

        val repeatMode: Preferences.Key<Int>
            get() = intPreferencesKey("repeat_mode")

        val isShuffle: Preferences.Key<Boolean>
            get() = booleanPreferencesKey("is_shuffle")

        val lastMusic: Preferences.Key<String>
            get() = stringPreferencesKey("last_music")
    }

    suspend inline fun <T> get(key: Preferences.Key<T>, def: T): T = DateStores.getDefault().get(key, def)
    suspend inline fun <T> put(key: Preferences.Key<T>, value: T) = DateStores.getDefault().put(key, value)
    fun <T> getBlocking(key: Preferences.Key<T>, def: T): T = DateStores.getDefault().getBlocking(key, def)
    fun <T> putBlocking(key: Preferences.Key<T>, value: T) = DateStores.getDefault().putBlocking(key, value)

    var useBuiltInBrowser by PrefState(Key.useBuiltInBrowser, false)
    var poemUpdateInterval by PrefState(Key.poemUpdateInterval, 24 * 60 * 60 * 1000)
}

