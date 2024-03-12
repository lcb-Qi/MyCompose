package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lcb.one.ui.widget.settings.ui.internal.SettingsScaffold

@Composable
fun SettingsMenuLink(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsScaffold(
            title = title,
            enabled = enabled,
            summary = summary,
            icon = icon,
            action = {
                Icon(Icons.AutoMirrored.Rounded.ArrowForwardIos, "")
            },
        )
    }
}
