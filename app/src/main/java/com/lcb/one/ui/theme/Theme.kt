package com.lcb.one.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

object ThemeManager {
    val seeds = defaultSeeds

    private var appThemeSeed by IntPrefState(
        key = "app_theme_seed",
        default = seeds.first().toArgb()
    )

    private var appDynamicColor by BooleanPrefState(
        key = MyApp.getAppContext().getString(R.string.settings_dynamic_color_key),
        default = true
    )

    fun changeDynamicColor(dynamicColor: Boolean) {
        appDynamicColor = dynamicColor
    }


    fun changeTheme(seed: Color) {
        appThemeSeed = seed.toArgb()
    }

    val currentTheme
        get() = Color(appThemeSeed)

    val dynamicColor
        get() = appDynamicColor
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        ThemeManager.dynamicColor -> {
            val context = LocalContext.current
            val colorScheme =
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

            colorScheme.whiteBackground(darkTheme)
        }

        else -> dynamicColorScheme(
            seedColor = ThemeManager.currentTheme,
            isDark = darkTheme,
            style = PaletteStyle.Vibrant
        ).whiteBackground(darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private fun ColorScheme.whiteBackground(darkTheme: Boolean): ColorScheme {
    return copy(
        background = if (darkTheme) Color.Black else Color.White,
        surface = if (darkTheme) Color.Black else Color.White,
    )
}
