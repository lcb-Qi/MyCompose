package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lcb.one.ui.widget.settings.ui.internal.SettingsScaffold

@Composable
fun SettingsSlider(
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
    SettingsScaffold(
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        summary = {
            Column {
                if (summary != null) {
                    Text(text = summary, style = MaterialTheme.typography.bodySmall)
                }
                Slider(
                    value = value,
                    onValueChange = { value -> onValueChange(value) },
                    enabled = enabled,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChangeFinished = onValueChangeFinished,
                )
            }
        },
        icon = icon,
    )
}