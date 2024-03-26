package com.lcb.one.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

object Route {
    const val HOME = "Home"
    const val TOOL = "Tool"
    const val MORE = "More"
    const val BILI = "BiliBili"
    const val DEVICE = "Device"
    const val SETTINGS = "Settings"
    const val APPS = "Apps"
}

@Stable
class RouteScreen(
    val route: String,
    val title: String,
    val content: @Composable () -> Unit
)