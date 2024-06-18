package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.getRouters
import com.lcb.one.ui.theme.AppTheme
import com.lcb.one.ui.widget.dialog.AssertInternetDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { AppScreen() }
        PoemAppWidgetProvider.tryUpdate(this)
    }

    @Composable
    private fun AppScreen() {
        CompositionLocalProvider(value = LocalNav provides rememberNavController()) {
            AppTheme {
                NavHost(
                    navController = LocalNav.current!!,
                    startDestination = Route.MAIN,
                    modifier = Modifier.fillMaxSize(),
                    enterTransition = { slideInHorizontally(animationSpec = tween(500)) { it } },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(500)) { -it } },
                    popEnterTransition = { slideInHorizontally(animationSpec = tween(500)) { -it } },
                    popExitTransition = { slideOutHorizontally(animationSpec = tween(500)) { it } },
                ) {
                    getRouters().forEach { routeItem ->
                        composable(routeItem.route) { routeItem.content() }
                    }
                }

                AssertInternetDialog(AppGlobalConfigs.assertNetwork)
            }
        }
    }
}