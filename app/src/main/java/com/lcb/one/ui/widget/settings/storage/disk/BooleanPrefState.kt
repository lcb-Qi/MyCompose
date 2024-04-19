package com.lcb.one.ui.widget.settings.storage.disk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberBooleanPrefState(key: String, default: Boolean) =
    remember { BooleanPrefState(key, default) }

class BooleanPrefState(key: String, default: Boolean) :
    PreferenceSettingState<Boolean>(key, default)
