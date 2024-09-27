package com.lcb.weight.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import com.lcb.weight.LegacySlider

@Composable
fun SettingsSlider(
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
    SettingsScaffold(
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
    )
}