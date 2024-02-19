package com.lcb.one.ui.widget.settings.ui.internal

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsTileScaffold(
    enabled: Boolean = true,
    title: @Composable () -> Unit,
    summary: @Composable (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    actionDivider: Boolean = false,
) {
    // val minHeight = if (subtitle == null) 72.dp else 88.dp
    val minHeight = 72.dp
    ListItem(
        modifier = Modifier.defaultMinSize(minHeight = minHeight),
        headlineText = { WrapContentColor(enabled) { title() } },
        supportingText = if (summary == null) {
            null
        } else {
            { WrapContentColor(enabled = enabled) { summary() } }
        },
        leadingContent = if (icon == null) {
            null
        } else {
            { WrapContentColor(enabled = enabled) { icon() } }
        },
        trailingContent = if (action == null) {
            null
        } else {
            { action(enabled) }
        },
    )
}
