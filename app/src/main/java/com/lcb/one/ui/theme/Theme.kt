package com.lcb.one.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

@Composable
fun AppThemeSurface(
    darkTheme: Boolean = ThemeManager.calculateDarkTheme(),
    dynamicColor: Boolean = ThemeManager.dynamicColor,
    seedColor: Color = ThemeManager.themeColor,
    amoledMode: Boolean = ThemeManager.amoledMode,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> dynamicColorScheme(
            seedColor = seedColor,
            isDark = darkTheme,
            style = PaletteStyle.Vibrant,
        )
    }.let {
        if (amoledMode) {
            it.copy(
                background = if (darkTheme) Color.Black else Color.White,
                surface = if (darkTheme) Color.Black else Color.White,
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