package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.ui.Route
import com.lcb.one.ui.getRouters
import com.lcb.one.ui.glance.PoemAppWidget
import com.lcb.one.ui.widget.AppNavHost
import com.lcb.one.ui.widget.AppThemeSurface
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppThemeSurface {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = Route.MAIN,
                    modifier = Modifier.fillMaxSize()
                ) {
                    getRouters(navController).forEach { routeItem ->
                        composable(routeItem.route) { routeItem.content() }
                    }
                }
            }
        }
        lifecycleScope.launch {
            PoemAppWidget().updateAll(this@MainActivity)
        }
    }
}