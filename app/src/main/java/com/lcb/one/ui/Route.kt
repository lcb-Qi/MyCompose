package com.lcb.one.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.lcb.one.ui.screen.about.AboutScreen
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationHistoryScreen
import com.lcb.one.ui.screen.player.MusicPlayerScreen
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.screen.qmc.QmcConverterScreen
import com.lcb.one.ui.screen.settings.SettingsScreen
import com.lcb.one.ui.screen.settings.ThemeSettingsScreen
import com.lcb.one.ui.screen.webview.WebScreen
import com.lcb.one.ui.screen.zxing.QrCodeScreen

object Route {
    val defaultScreens by lazy {
        listOf(
            MainScreen,
            BiliBiliScreen,
            DeviceInfoScreen,
            SettingsScreen,
            AboutScreen,
            InstalledAppsScreen,
            MenstruationAssistantScreen,
            MenstruationHistoryScreen,
            ThemeSettingsScreen,
            PrivacyScreen,
            QrCodeScreen,
            WebScreen,
            QmcConverterScreen,
            MusicPlayerScreen,
        )
    }

    val supportShortcutScreens by lazy {
        defaultScreens.filter {
            it !is WebScreen && it !is MenstruationHistoryScreen && it !is MainScreen
        }
    }
}

@Stable
interface Screen {
    val route: String
    val args: List<NamedNavArgument>
        get() = emptyList()

    val label: String
        get() = route

    @Composable
    fun Content(navController: NavHostController, args: Bundle?)
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

fun NavHostController.launchSingleTop(screen: Screen, extras: Navigator.Extras? = null) {
    navigate(
        route = screen.route,
        navOptions = navOptions {
            launchSingleTop = true
        },
        navigatorExtras = extras
    )
}