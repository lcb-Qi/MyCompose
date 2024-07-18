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

const val ANIMATE_DURATION = 500

val LocalNav: ProvidableCompositionLocal<NavHostController?> = staticCompositionLocalOf { null }

@Composable
fun AppNavHost(
    start: String,
    screens: List<Screen>,
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