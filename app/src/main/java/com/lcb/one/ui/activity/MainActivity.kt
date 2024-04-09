package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.Route
import com.lcb.one.ui.getRouters
import com.lcb.one.ui.glance.PoemAppWidget
import com.lcb.one.ui.theme.AppTheme
import com.lcb.one.ui.widget.dialog.AssertInternetDialog
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
            AssertInternetDialog()
        }
        lifecycleScope.launch {
            PoemAppWidget().updateAll(this@MainActivity)
        }
    }

    @Composable
    private fun AppScreen() {
        AppTheme(dynamicColor = AppGlobalConfigs.appDynamicColor) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Route.MAIN,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { slideInHorizontally(animationSpec = tween(500)) { it } },
                exitTransition = { slideOutHorizontally(animationSpec = tween(500)) { -it } },
                popEnterTransition = { slideInHorizontally(animationSpec = tween(500)) { -it } },
                popExitTransition = { slideOutHorizontally(animationSpec = tween(500)) { it } },
            ) {
                getRouters(navController).forEach { routeItem ->
                    composable(routeItem.route) { routeItem.content() }
                }
            }
        }
    }
}