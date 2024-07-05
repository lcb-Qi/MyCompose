package com.lcb.one.ui.widget.settings.ui

import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsSwitch

@Composable
fun SimpleSettingsSwitch(
    checked: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: String? = null,
    colors: ListItemColors = SettingsDefaults.colors(),
    onCheckedChange: (Boolean) -> Unit = {},
) {
    SettingsSwitch(
        modifier = modifier.minSettingsHeight(),
        colors = colors,
        state = checked,
        title = { SettingsDefaults.Title(title = title) },
        icon = icon,
        enabled = enabled,
        subtitle = summary?.let { { SettingsDefaults.Summary(summary = summary) } },
        onCheckedChange = onCheckedChange
    )
}
