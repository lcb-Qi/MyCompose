package com.lcb.one.prefs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lcb.one.prefs.DateStores.get
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

@Composable
fun <T> rememberPrefState(key: Preferences.Key<T>, default: T) = remember(key) { PrefState(key, default) }

class PrefState<T>(
    private val key: Preferences.Key<T>,
    default: T,
    private val dataStore: DataStore<Preferences> = DateStores.getDefault(),
) {
    private var _value: T by mutableStateOf(runBlocking { dataStore.get(key, default) })

    @OptIn(DelicateCoroutinesApi::class)
    var value: T
        set(value) {
            _value = value
            GlobalScope.launch { dataStore.edit { it[key] = value } }
        }
        get() = _value
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> PrefState<T>.getValue(thisObj: Any?, prop: KProperty<*>): T {
    return value
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> PrefState<T>.setValue(thisObj: Any?, prop: KProperty<*>, value: T) {
    this.value = value
}