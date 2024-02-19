package com.lcb.one.ui.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lcb.one.ui.page.AppSettings
import com.lcb.one.ui.theme.AppTheme

var appDynamicColor by mutableStateOf(AppSettings.getAppDynamicColor())

@Composable
fun AppThemeSurface(content: @Composable () -> Unit) {
    AppTheme(dynamicColor = appDynamicColor) {
        Surface(modifier = Modifier.fillMaxSize()) { content() }
    }
}