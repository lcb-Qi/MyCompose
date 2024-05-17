package com.lcb.one.ui.widget.settings.storage.disk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberFloatPrefState(key: String, default: Float = 0f) =
    remember { FloatPrefState(key, default) }

class FloatPrefState(key: String, default: Float) : PreferenceState<Float>(key, default)
