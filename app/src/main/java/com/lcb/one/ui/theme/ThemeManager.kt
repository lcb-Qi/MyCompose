package com.lcb.one.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.UserPref
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

object ThemeManager {
    val seeds = defaultSeeds

    // 主题种子
    private var themeSeed by IntPrefState(
        UserPref.Key.APP_THEME_SEED,
        seeds.first().toArgb()
    )

    // 主题色
    var themeColor = Color(themeSeed)
        get() = Color(themeSeed)
        set(value) {
            field = value
            themeSeed = value.toArgb()
        }

    // 是否动态取色
    var dynamicColor by BooleanPrefState(UserPref.Key.APP_DYNAMIC_COLOR, true)

    // 是否启用纯白/纯黑背景
    var amoledMode by BooleanPrefState(UserPref.Key.APP_ALOMED_MODE, false)
}

@Composable
fun createColorTheme(
    seedColor: Color = ThemeManager.themeColor,
    paletteStyle: PaletteStyle = PaletteStyle.Vibrant,
    dynamicColor: Boolean = ThemeManager.dynamicColor,
    darkTheme: Boolean = isSystemInDarkTheme(),
    isExtendedFidelity: Boolean = false,
    amoledMode: Boolean = ThemeManager.amoledMode
): ColorScheme {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> dynamicColorScheme(
            seedColor = seedColor,
            isDark = darkTheme,
            style = paletteStyle,
            isExtendedFidelity = isExtendedFidelity
        )
    }

    return if (amoledMode) colorScheme.withAmoledMode(darkTheme) else colorScheme
}


private fun ColorScheme.withAmoledMode(darkTheme: Boolean): ColorScheme {
    return copy(
        background = if (darkTheme) Color.Black else Color.White,
        surface = if (darkTheme) Color.Black else Color.White,
    )
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = createColorTheme(darkTheme = darkTheme),
        typography = Typography,
        content = { Surface(modifier = Modifier.fillMaxSize(), content = { content() }) }
    )
}
