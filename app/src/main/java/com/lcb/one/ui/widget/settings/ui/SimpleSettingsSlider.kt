package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.base.internal.SettingsTileScaffold
import com.lcb.one.ui.widget.common.LegacySlider

@Composable
fun SimpleSettingsSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    summary: String? = null,
    colors: ListItemColors = SettingsDefaults.colorsDefault(),
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    SettingsTileScaffold(
        modifier = Modifier.minSettingsHeight(),
        enabled = enabled,
        title = { SettingsDefaults.Title(title = title) },
        subtitle = {
            Column {
                summary?.let { SettingsDefaults.Summary(summary = summary) }
                LegacySlider(
                    value = value,
                    onValueChange = { value -> onValueChange(value) },
                    enabled = enabled,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChangeFinished = onValueChangeFinished,
                    colors = SliderDefaults.colors(),
                )
            }
        },
        icon = icon,
        colors = colors,
        tonalElevation = ListItemDefaults.Elevation,
        shadowElevation = ListItemDefaults.Elevation,
    )
}