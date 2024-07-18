package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.AppNavHost
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.theme.AppTheme
import com.lcb.one.ui.widget.dialog.AssertInternetDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { AppScreen(intent.getStringExtra("route")) }
        PoemAppWidgetProvider.tryUpdate(this)
    }

    @Composable
    private fun AppScreen(startRoute: String? = null) {
        AppTheme {
            AppNavHost(start = startRoute ?: MainScreen.route) {
                AssertInternetDialog(AppGlobalConfigs.assertNetwork)
            }
        }
    }
}