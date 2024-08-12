package com.lcb.one.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lcb.one.ui.MyApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.File

object DateStores {
    private val defaultDataStore by lazy {
        PreferenceDataStoreFactory.create {
            val rootDir = File(MyApp.get().dataDir, "prefs")

            File(rootDir, "default_prefs.preferences_pb")
        }
    }

    fun getDefault() = defaultDataStore

    fun <T> DataStore<Preferences>.getBlocking(key: Preferences.Key<T>, def: T): T = runBlocking {
        data.map { it[key] }.first() ?: def
    }

    fun <T> DataStore<Preferences>.putBlocking(key: Preferences.Key<T>, value: T) = runBlocking {
        edit { it[key] = value }
    }

    suspend inline fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, def: T): T {
        return data.map { it[key] }.first() ?: def
    }

    suspend inline fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
        edit { it[key] = value }
    }
}