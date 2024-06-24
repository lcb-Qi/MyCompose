package com.lcb.one.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.lcb.one.ui.screen.about.AboutScreen
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationHistoryScreen
import com.lcb.one.ui.screen.settings.ThemeSettingsScreen
import com.lcb.one.ui.screen.settings.SettingsScreen

const val ANIMATE_DURATION = 500

object Route {
    const val MAIN = "Main"
    const val BILI = "BiliBili"
    const val DEVICE = "Device"
    const val SETTINGS = "Settings"
    const val ABOUT = "About"
    const val APPS = "Apps"
    const val THEME = "Theme"
    const val MENSTRUAL_CYCLE_ASSISTANT = "MenstrualCycleAssistant"
    const val MENSTRUAL_CYCLE_HISTORY = "MenstrualCycleHistory"
}

@Stable
class RouteScreen(
    val route: String,
    val content: @Composable () -> Unit
)

val LocalNav: ProvidableCompositionLocal<NavHostController?> = staticCompositionLocalOf { null }

fun getRouters(): List<RouteScreen> {
    return listOf(
        RouteScreen(Route.MAIN) { MainScreen() },
        RouteScreen(Route.BILI) { BiliBiliScreen() },
        RouteScreen(Route.DEVICE) { DeviceInfoScreen() },
        RouteScreen(Route.SETTINGS) { SettingsScreen() },
        RouteScreen(Route.ABOUT) { AboutScreen() },
        RouteScreen(Route.APPS) { InstalledAppsScreen() },
        RouteScreen(Route.MENSTRUAL_CYCLE_ASSISTANT) { MenstruationAssistantScreen() },
        RouteScreen(Route.MENSTRUAL_CYCLE_HISTORY) { MenstruationHistoryScreen() },
        RouteScreen(Route.THEME) { ThemeSettingsScreen() },
    )
}