package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.minSettingsHeight() = this.defaultMinSize(minHeight = 64.dp)

val localSettingsItemColor: ProvidableCompositionLocal<ListItemColors?> = compositionLocalOf {
    null
}

@Composable
fun ProvideSettingsItemColor(
    containerColor: ListItemColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(localSettingsItemColor provides containerColor, content = content)
}

object SettingsDefaults {
    @Composable
    fun Title(modifier: Modifier = Modifier, title: String) {
        Text(modifier = modifier, text = title, style = MaterialTheme.typography.titleMedium)
    }

    @Composable
    fun Summary(modifier: Modifier = Modifier, summary: String) {
        Text(modifier = modifier, text = summary, style = MaterialTheme.typography.bodyMedium)
    }

    @Composable
    fun colors(): ListItemColors = localSettingsItemColor.current ?: ListItemDefaults.colors()

    @Composable
    fun colorOnCard(): ListItemColors {
        return colorOnContainer(CardDefaults.cardColors().containerColor)
    }

    @Composable
    fun colorOnContainer(containerColor: Color): ListItemColors {
        return ListItemDefaults.colors(containerColor = containerColor)
    }
}