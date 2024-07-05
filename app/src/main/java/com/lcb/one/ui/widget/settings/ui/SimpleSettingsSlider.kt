package com.lcb.one.ui.widget.settings.ui

import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsSlider

@Composable
fun SimpleSettingsSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    summary: String? = null,
    colors: ListItemColors = SettingsDefaults.colors(),
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    SettingsSlider(
        colors = colors,
        modifier = Modifier.minSettingsHeight(),
        enabled = enabled,
        title = { SettingsDefaults.Title(title = title) },
        subtitle = summary?.let { { SettingsDefaults.Summary(summary = summary) } },
        icon = icon,
        valueRange = valueRange,
        steps = steps,
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished
    )
}