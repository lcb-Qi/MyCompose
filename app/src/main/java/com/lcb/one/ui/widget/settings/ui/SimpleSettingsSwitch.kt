package com.lcb.one.ui.widget.settings.ui

import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.alorma.compose.settings.ui.SettingsSwitch

@Composable
fun SimpleSettingsSwitch(
    checked: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: String? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    onCheckedChange: (Boolean) -> Unit = {},
) {
    SettingsSwitch(
        modifier = modifier.minSettingsHeight(),
        colors = colors,
        state = checked,
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        icon = icon,
        enabled = enabled,
        subtitle = summary?.let { { Text(text = it) } },
        onCheckedChange = onCheckedChange
    )
}
