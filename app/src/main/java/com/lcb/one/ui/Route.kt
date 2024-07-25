package com.lcb.one.ui

import android.os.Bundle
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lcb.one.ui.screen.player.MusicPlayerScreen
import com.lcb.one.ui.screen.about.AboutScreen
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationHistoryScreen
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.screen.qmc.QmcConverterScreen
import com.lcb.one.ui.screen.settings.SettingsScreen
import com.lcb.one.ui.screen.settings.ThemeSettingsScreen
import com.lcb.one.ui.screen.webview.WebScreen
import com.lcb.one.ui.screen.zxing.QrCodeScreen

const val ANIMATE_DURATION = 500

val LocalNav: ProvidableCompositionLocal<NavHostController?> = staticCompositionLocalOf { null }

private val defaultScreens by lazy {
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

@Composable
fun AppNavHost(
    start: String,
    screens: List<Screen> = defaultScreens,
    navHostController: NavHostController = rememberNavController(),
    content: @Composable () -> Unit = {}
) {
    CompositionLocalProvider(LocalNav provides navHostController) {
        NavHost(
            navController = LocalNav.current!!,
            startDestination = start,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { slideInHorizontally(tween(ANIMATE_DURATION)) { it } },
            exitTransition = { slideOutHorizontally(tween(ANIMATE_DURATION)) { -it } },
            popEnterTransition = { slideInHorizontally(tween(ANIMATE_DURATION)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(ANIMATE_DURATION)) { it } },
            builder = {
                screens.forEach { screen ->
                    composable(
                        route = screen.route,
                        arguments = screen.args,
                        content = { screen.Content(it.arguments) }
                    )
                }
            }
        )

        content()
    }
}

@Stable
interface Screen {
    val route: String
    val args: List<NamedNavArgument>
        get() = emptyList()

    @Composable
    fun Content(args: Bundle?)
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