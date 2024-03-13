package com.lcb.one.ui.widget

import androidx.compose.foundation.isSystemInDarkTheme
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
fun AppThemeSurface(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = appDynamicColor,
    content: @Composable () -> Unit
) {
    AppTheme(darkTheme, dynamicColor) {
        Surface(modifier = Modifier.fillMaxSize()) { content() }
    }
}