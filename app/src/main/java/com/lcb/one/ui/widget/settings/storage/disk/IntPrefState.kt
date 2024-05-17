package com.lcb.one.ui.widget.settings.storage.disk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberIntPrefState(key: String, default: Int = 0) = remember { IntPrefState(key, default) }

class IntPrefState(key: String, default: Int) : PreferenceState<Int>(key, default)
