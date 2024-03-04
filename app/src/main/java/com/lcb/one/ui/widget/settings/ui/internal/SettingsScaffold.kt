package com.lcb.one.ui.widget.settings.ui.internal

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingsScaffold(
    enabled: Boolean = true,
    title: @Composable () -> Unit,
    summary: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    action: @Composable ((Boolean) -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.defaultMinSize(minHeight = 72.dp),
        headlineContent = { WrapContentColor(enabled) { title() } },
        supportingContent = if (summary == null) {
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
            { WrapContentColor(enabled) { action(enabled) } }
        },
    )
}

@Composable
private fun WrapContentColor(
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    val alpha = if (enabled) {
        1.0f
    } else {
        0.6f
    }
    val contentColor = LocalContentColor.current.copy(alpha = alpha)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        content()
    }
}
