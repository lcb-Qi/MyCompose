package com.lcb.one.ui.screen.applist.repo

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable

@Stable
data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val isSystemApp: Boolean
)