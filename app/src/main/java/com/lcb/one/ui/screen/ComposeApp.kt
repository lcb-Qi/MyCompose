package com.lcb.one.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.Route
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.theme.AppThemeSurface
import com.lcb.one.ui.widget.dialog.AssertInternetDialog

const val ANIMATE_DURATION = 500

@Composable
fun ComposeApp(startRoute: String? = null) {

    AppThemeSurface {
        val navHostController = rememberNavController()
        NavHost(
            navController = navHostController,
            startDestination = startRoute ?: MainScreen.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { slideInHorizontally(tween(ANIMATE_DURATION)) { it } },
            exitTransition = { slideOutHorizontally(tween(ANIMATE_DURATION)) { -it } },
            popEnterTransition = { slideInHorizontally(tween(ANIMATE_DURATION)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(ANIMATE_DURATION)) { it } },
            builder = {
                Route.defaultScreens.forEach { screen ->
                    composable(
                        route = screen.route,
                        arguments = screen.args,
                        content = { screen.Content(navHostController, it.arguments) }
                    )
                }
            }
        )

        AssertInternetDialog(AppGlobalConfigs.assertNetwork)
    }
}