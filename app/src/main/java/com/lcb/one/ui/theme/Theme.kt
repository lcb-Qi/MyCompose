package com.lcb.one.ui.theme

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppThemeSurface(
    darkTheme: Boolean = ThemeManager.calculateDarkTheme(),
    dynamicColor: Boolean = ThemeManager.dynamicColor,
    amoled: Boolean = ThemeManager.amoled,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }.let {
        if (amoled) {
            it.copy(
                background = if (darkTheme) Color.Black else Color.White,
                surface = if (darkTheme) Color.Black else Color.White,

                onBackground = if (darkTheme) Color.White else Color.Black,
                onSurface = if (darkTheme) Color.White else Color.Black,
            )
        } else {
            it
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = { Surface(modifier = Modifier.fillMaxSize(), content = { content() }) }
    )
}