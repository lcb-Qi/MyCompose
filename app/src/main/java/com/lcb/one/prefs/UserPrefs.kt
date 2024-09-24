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

object UserPrefs {
    object Key {
        // 动态取色
        val dynamicColor = booleanPreferencesKey("dynamic_color")

        // amoled（纯白/纯黑背景）
        val amoled = booleanPreferencesKey("amoled_mode")

        // 深色模式
        val uiMode = intPreferencesKey("ui_mode")

        // 时钟字体大小
        val clockSize = floatPreferencesKey("clock_size")

        // 时钟屏幕布局
        val clockLayout = intPreferencesKey("clock_layout")

        // 快捷方式
        val shortcuts = stringSetPreferencesKey("shortcuts")

        // 首页头图
        val homeHeader = stringPreferencesKey("home_header")

        // 播放器重复方式
        val repeatMode = intPreferencesKey("repeat_mode")

        // 是否随机播放
        val isShuffle = booleanPreferencesKey("is_shuffle")

        // 上次播放音乐
        val lastMusic = stringPreferencesKey("last_music")
    }

    suspend inline fun <T> get(key: Preferences.Key<T>, def: T): T =
        DateStores.getDefault().get(key, def)

    suspend inline fun <T> put(key: Preferences.Key<T>, value: T) =
        DateStores.getDefault().put(key, value)

    fun <T> getBlocking(key: Preferences.Key<T>, def: T): T =
        DateStores.getDefault().getBlocking(key, def)

    fun <T> putBlocking(key: Preferences.Key<T>, value: T) =
        DateStores.getDefault().putBlocking(key, value)
}

