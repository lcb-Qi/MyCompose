package com.lcb.one.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.lcb.one.ui.screen.AppListScreen
import com.lcb.one.ui.screen.MainScreen
import com.lcb.one.ui.screen.BiliBiliScreen
import com.lcb.one.ui.screen.DeviceInfoScreen
import com.lcb.one.ui.screen.SettingsScreen

object Route {
    const val MAIN = "Main"
    const val BILI = "BiliBili"
    const val DEVICE = "Device"
    const val SETTINGS = "Settings"
    const val APPS = "Apps"
}

@Stable
class RouteScreen(
    val route: String,
    val content: @Composable () -> Unit
)

fun getRouters(navController: NavHostController): List<RouteScreen> {
    return listOf(
        RouteScreen(Route.MAIN) { MainScreen(navController) },
        RouteScreen(Route.BILI) { BiliBiliScreen() },
        RouteScreen(Route.DEVICE) { DeviceInfoScreen() },
        RouteScreen(Route.SETTINGS) { SettingsScreen() },
        RouteScreen(Route.APPS) { AppListScreen() },
    )
}