package com.lcb.one.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.lcb.one.ui.screen.clock.ClockScreen
import com.lcb.one.ui.screen.about.AboutScreen
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationHistoryScreen
import com.lcb.one.ui.screen.player.MusicPlayerScreen
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.screen.qmc.QmcConverterScreen
import com.lcb.one.ui.screen.settings.SettingsScreen

object Route {
    val defaultScreens by lazy {
        setOf(
            MainScreen,
            DeviceInfoScreen,
            SettingsScreen,
            AboutScreen,
            InstalledAppsScreen,
            MenstruationAssistantScreen,
            MenstruationHistoryScreen,
            PrivacyScreen,
            QmcConverterScreen,
            MusicPlayerScreen,
            ClockScreen
        )
    }

    val supportShortcutScreens by lazy {
        val notSupport = setOf(MainScreen, MenstruationHistoryScreen)
        defaultScreens - notSupport
    }
}

@Stable
abstract class Screen {
    open val route: String = this.javaClass.name
    open val args: List<NamedNavArgument> = emptyList()

    open val label: String = this.javaClass.simpleName

    @Composable
    abstract fun Content(navController: NavHostController, args: Bundle?)
}

fun NavHostController.launchSingleTop(route: String, extras: Navigator.Extras? = null) {
    navigate(
        route = route,
        navOptions = navOptions {
            launchSingleTop = true
        },
        navigatorExtras = extras
    )
}

fun NavHostController.launchSingleTop(screen: Screen, extras: Navigator.Extras? = null) =
    launchSingleTop(screen.route, extras)