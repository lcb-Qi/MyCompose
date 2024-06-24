package com.lcb.one.ui.widget.settings.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.alorma.compose.settings.ui.SettingsSlider

@Composable
fun SimpleSettingsSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    summary: String? = null,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    SettingsSlider(
        modifier = Modifier.minSettingsHeight(),
        enabled = enabled,
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        subtitle = summary?.let { { Text(text = it) } },
        icon = icon,
        valueRange = valueRange,
        steps = steps,
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished
    )
}