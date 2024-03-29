package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.lcb.one.ui.widget.settings.ui.internal.SettingsScaffold

@Composable
fun SettingsSwitch(
    checked: Boolean,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    switchColors: SwitchColors = SwitchDefaults.colors(),
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val update: (Boolean) -> Unit = { boolean -> onCheckedChange(boolean) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                enabled = enabled,
                value = checked,
                role = Role.Switch,
                onValueChange = { update(!checked) },
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsScaffold(
            enabled = enabled,
            title = title,
            summary = summary,
            icon = icon,
            action = {
                Switch(
                    enabled = enabled,
                    checked = checked,
                    onCheckedChange = update,
                    colors = switchColors,
                )
            },
        )
    }
}
