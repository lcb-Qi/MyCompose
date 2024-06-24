package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsGroup

@Composable
fun SimpleSettingsGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    SettingsGroup(
        modifier = modifier,
        title = title?.let { { SettingsCategory(title = it) } },
        content = content
    )
}

@Composable
fun SettingsCategory(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}