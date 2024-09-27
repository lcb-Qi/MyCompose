package com.lcb.weight.settings

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
fun SettingsSwitch(
    checked: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: String? = null,
    colors: ListItemColors = SettingsDefaults.colorsDefault(),
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val update: (Boolean) -> Unit = { boolean -> onCheckedChange(boolean) }

    SettingsScaffold(
        modifier = Modifier
            .toggleable(
                enabled = enabled,
                value = checked,
                role = Role.Switch,
                onValueChange = { update(!checked) },
            )
            .then(modifier),
        enabled = enabled,
        title = { SettingsDefaults.Title(title = title) },
        subtitle = summary?.let { { SettingsDefaults.Summary(summary = summary) } },
        icon = icon,
        colors = colors,
    ) {
        Switch(
            enabled = enabled,
            checked = checked,
            onCheckedChange = update,
        )
    }
}
