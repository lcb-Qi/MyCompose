package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.alorma.compose.settings.ui.base.internal.SettingsTileScaffold

@Composable
fun SettingsSimpleText(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    summary: String? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
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
