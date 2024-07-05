package com.lcb.one.ui.widget.settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsMenuLink

@Composable
fun SimpleSettingsMenuLink(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    summary: String? = null,
    action: (@Composable () -> Unit)? = { Icon(Icons.AutoMirrored.Rounded.NavigateNext, null) },
    colors: ListItemColors = SettingsDefaults.colors(),
    onClick: () -> Unit,
) {
    SettingsMenuLink(
        modifier = modifier.minSettingsHeight(),
        colors = colors,
        enabled = enabled,
        title = { SettingsDefaults.Title(title = title) },
        subtitle = summary?.let { { SettingsDefaults.Summary(summary = it) } },
        icon = icon,
        action = action,
        onClick = onClick
    )
}
