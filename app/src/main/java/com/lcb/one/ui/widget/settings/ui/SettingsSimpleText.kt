package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.base.internal.SettingsTileScaffold

@Composable
fun SettingsSimpleText(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    summary: String? = null,
    colors: ListItemColors = SettingsDefaults.colorsDefault(),
    onClick: () -> Unit,
) {

    SettingsTileScaffold(
        enabled = enabled,
        colors = colors,
        modifier = Modifier
            .minSettingsHeight()
            .clickable(enabled = enabled, onClick = onClick)
            .then(modifier),
        title = { SettingsDefaults.Title(title = title) },
        subtitle = summary?.let { { SettingsDefaults.Summary(summary = summary) } },
        icon = icon,
        action = null,
    )
}
