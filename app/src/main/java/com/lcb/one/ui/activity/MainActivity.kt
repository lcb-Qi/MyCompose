package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.screen.ComposeApp

class MainActivity : ComponentActivity() {
    companion object {
        const val EXT_START_ROUTE = "start_route"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val startRoute = intent.getStringExtra(EXT_START_ROUTE)
        setContent { ComposeApp(startRoute) }
        PoemAppWidgetProvider.tryUpdate(this)
    }
}