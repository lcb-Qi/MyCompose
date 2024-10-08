package com.lcb.weight.settings

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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
    fun colorsDefault(): ListItemColors = ListItemDefaults.colors()

    @Composable
    fun colorsOnCard(): ListItemColors = colorsOnContainer(CardDefaults.cardColors().containerColor)

    @Composable
    fun colorsOnContainer(containerColor: Color): ListItemColors =
        ListItemDefaults.colors(containerColor = containerColor)
}