package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.route.NavGraphs
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.navController
import com.lcb.one.ui.theme.AppTheme
import com.lcb.one.ui.widget.dialog.AssertInternetDialog
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { AppScreen() }
        PoemAppWidgetProvider.tryUpdate(this)
    }

    @Composable
    private fun AppScreen() {
        AppTheme {
            val controller = rememberNavController()
            navController = controller.toDestinationsNavigator()
            DestinationsNavHost(navGraph = NavGraphs.app, navController = controller)
            AssertInternetDialog(AppGlobalConfigs.assertNetwork)
        }
    }
}