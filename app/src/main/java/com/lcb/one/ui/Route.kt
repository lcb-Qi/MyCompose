package com.lcb.one.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.lcb.one.ui.screen.applist.AppListScreen
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.mcAssistant.MenstrualCycleAssistantScreen
import com.lcb.one.ui.screen.mcAssistant.MenstrualCycleHistoryScreen
import com.lcb.one.ui.screen.settings.SettingsScreen

object Route {
    const val MAIN = "Main"
    const val BILI = "BiliBili"
    const val DEVICE = "Device"
    const val SETTINGS = "Settings"
    const val APPS = "Apps"
    const val MENSTRUAL_CYCLE_ASSISTANT = "MenstrualCycleAssistant"
    const val MENSTRUAL_CYCLE_HISTORY = "MenstrualCycleHistory"
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
        RouteScreen(Route.MENSTRUAL_CYCLE_ASSISTANT) { MenstrualCycleAssistantScreen(navController) },
        RouteScreen(Route.MENSTRUAL_CYCLE_HISTORY) { MenstrualCycleHistoryScreen() },
    )
}