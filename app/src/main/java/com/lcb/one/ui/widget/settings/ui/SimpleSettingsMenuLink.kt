package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsMenuLink

@Composable
fun SimpleSettingsMenuLink(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    summary: String? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    onClick: () -> Unit,
) {
    SettingsMenuLink(
        modifier = modifier.minSettingsHeight(),
        colors = colors,
        enabled = enabled,
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        subtitle = summary?.let { { Text(text = summary) } },
        icon = icon,
        action = {
            Icon(Icons.AutoMirrored.Rounded.NavigateNext, null)
        },
        onClick = onClick
    )
}

private val defaultSettingsMiniHeight = 64.dp
fun Modifier.minSettingsHeight() =
    this.defaultMinSize(minHeight = defaultSettingsMiniHeight)
