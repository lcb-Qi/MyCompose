package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.AppNavHost
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.screen.about.AboutScreen
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.MainScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationHistoryScreen
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.screen.settings.SettingsScreen
import com.lcb.one.ui.screen.settings.ThemeSettingsScreen
import com.lcb.one.ui.screen.webview.WebScreen
import com.lcb.one.ui.screen.zxing.QrCodeScreen
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
            AppNavHost(start = startRoute ?: MainScreen.route, screens = allScreens) {
                AssertInternetDialog(AppGlobalConfigs.assertNetwork)
            }
        }
    }
}

private val allScreens by lazy {
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
        WebScreen
    )
}