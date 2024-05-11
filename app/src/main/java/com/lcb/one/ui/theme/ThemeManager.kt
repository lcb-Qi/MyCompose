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
import androidx.glance.color.DynamicThemeColorProviders.primary
import androidx.glance.color.DynamicThemeColorProviders.primaryContainer
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.theme.ThemeManager.dynamicColor
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.disk.StringPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.ktx.toColor
import com.materialkolor.ktx.toHct

object ThemeManager {
    val seeds = defaultSeeds

    // 主题种子色
    private var themeSeed by IntPrefState(
        key = "app_theme_seed",
        default = seeds.first().toArgb()
    )

    // 主题色
    var themeColor = Color(themeSeed)
        get() = Color(themeSeed)
        set(value) {
            field = value
            themeSeed = value.toArgb()
        }

    // 是否动态取色
    var dynamicColor by BooleanPrefState(
        key = MyApp.getAppContext().getString(R.string.settings_dynamic_color_key),
        default = true
    )

    // 调色版风格，影响种子色生成的主题色彩风格
    private var _paletteStyle by StringPrefState(
        key = "app_palette_style",
        PaletteStyle.Vibrant.name
    )

    var paletteStyle = PaletteStyle.valueOf(_paletteStyle)
        get() = PaletteStyle.valueOf(_paletteStyle)
        set(value) {
            field = value
            _paletteStyle = value.name
        }

    var isExtendedFidelity = false

    // 是否启用纯白/纯黑背景
    var amoledMode by BooleanPrefState(
        key = "app_amoled_mode",
        default = false
    )
}

@Composable
fun createColorTheme(
    seedColor: Color = ThemeManager.themeColor,
    paletteStyle: PaletteStyle = ThemeManager.paletteStyle,
    dynamicColor: Boolean = ThemeManager.dynamicColor,
    darkTheme: Boolean = isSystemInDarkTheme(),
    isExtendedFidelity: Boolean = ThemeManager.isExtendedFidelity,
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
        content = content
    )
}
