package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lcb.one.ui.widget.settings.ui.internal.SettingsScaffold

@Composable
fun SettingsSimpleText(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    summary: String? = null,
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
            enabled = enabled,
            title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
            summary = {
                if (summary != null) {
                    Text(text = summary, style = MaterialTheme.typography.bodySmall)
                }
            },
            icon = icon,
            action = null,
        )
    }
}
