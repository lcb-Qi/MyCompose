package com.lcb.weight.settings

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsMenuLink(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    summary: String? = null,
    action: (@Composable () -> Unit)? = { Icon(Icons.AutoMirrored.Rounded.NavigateNext, null) },
    colors: ListItemColors = SettingsDefaults.colorsDefault(),
    onClick: () -> Unit,
) {
    SettingsScaffold(
        modifier = Modifier
            .clickable(
                enabled = enabled,
                onClick = onClick,
            )
            .then(modifier),
        enabled = enabled,
        title = { SettingsDefaults.Title(title = title) },
        subtitle = summary?.let { { SettingsDefaults.Summary(summary = it) } },
        icon = icon,
        colors = colors,
        action = action,
    )
}
