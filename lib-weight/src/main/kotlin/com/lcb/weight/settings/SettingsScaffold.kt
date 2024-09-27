package com.lcb.weight.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private const val DISABLED_LABEL_TEXT_OPACITY = 0.3f
private const val DISABLED_LEADING_ICON_OPACITY = 0.38f
private const val DISABLED_TRAILING_ICON_OPACITY = 0.38f

@Composable
internal fun SettingsScaffold(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    subtitle: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    action: @Composable (() -> Unit)? = null,
) {
    val decoratedTitle: @Composable () -> Unit = {
        ProvideContentColor(labelContentColor(enabled)) {
            title()
        }
    }
    val decoratedSubtitle: @Composable (() -> Unit)? = subtitle?.let {
        {
            ProvideContentColor(labelContentColor(enabled)) {
                subtitle()
            }
        }
    }
    val decoratedIcon: @Composable (() -> Unit)? = icon?.let {
        {
            ProvideContentColor(iconContentColor(enabled)) {
                icon()
            }
        }
    }
    val decoratedAction: @Composable (() -> Unit)? = action?.let {
        {
            ProvideContentColor(actionContentColor(enabled)) {
                action()
            }
        }
    }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            // .defaultMinSize(minHeight = 64.dp)
            .then(modifier),
        headlineContent = decoratedTitle,
        supportingContent = decoratedSubtitle,
        leadingContent = decoratedIcon,
        trailingContent = decoratedAction,
        colors = colors,
    )
}

@ReadOnlyComposable
@Composable
private fun labelContentColor(enabled: Boolean): Color {
    return LocalContentColor.current.let {
        if (enabled) it else it.copy(alpha = DISABLED_LABEL_TEXT_OPACITY)
    }
}

@ReadOnlyComposable
@Composable
private fun iconContentColor(enabled: Boolean): Color {
    return LocalContentColor.current.let {
        if (enabled) it else it.copy(alpha = DISABLED_LEADING_ICON_OPACITY)
    }
}

@ReadOnlyComposable
@Composable
private fun actionContentColor(enabled: Boolean): Color {
    return LocalContentColor.current.let {
        if (enabled) it else it.copy(alpha = DISABLED_TRAILING_ICON_OPACITY)
    }
}

@Composable
private fun ProvideContentColor(
    contentColor: Color,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        content()
    }
}
