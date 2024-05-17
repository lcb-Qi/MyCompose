package com.lcb.one.ui.widget.settings.storage.disk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberStringPrefState(key: String, default: String = "") =
    remember { StringPrefState(key, default) }

class StringPrefState(key: String, default: String) : PreferenceState<String>(key, default)
