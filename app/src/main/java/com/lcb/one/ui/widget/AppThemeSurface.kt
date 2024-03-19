package com.lcb.one.ui.widget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lcb.one.ui.AppSettings
import com.lcb.one.ui.theme.AppTheme

@Composable
fun AppThemeSurface(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = AppSettings.appDynamicColor,
    content: @Composable () -> Unit
) {
    AppTheme(darkTheme, dynamicColor) {
        Surface(modifier = Modifier.fillMaxSize()) { content() }
    }
}