package com.lcb.one.ui.widget.settings.storage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lcb.one.prefs.DateStores
import com.lcb.one.prefs.DateStores.getBlocking
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class DataStoreState<T>(
    private val key: Preferences.Key<T>,
    default: T,
    private val dataStore: DataStore<Preferences> = DateStores.getDefault(),
) {
    private var _value: T by mutableStateOf(dataStore.getBlocking(key, default))

    @OptIn(DelicateCoroutinesApi::class)
    var value: T
        set(value) {
            _value = value
            GlobalScope.launch { dataStore.edit { it[key] = value } }
        }
        get() = _value
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> DataStoreState<T>.getValue(thisObj: Any?, prop: KProperty<*>): T {
    return value
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> DataStoreState<T>.setValue(thisObj: Any?, prop: KProperty<*>, value: T) {
    this.value = value
}