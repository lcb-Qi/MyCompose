package com.lcb.one.ui.screen.device.widget

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults

@Composable
fun SimpleInfoCard(modifier: Modifier = Modifier, data: Map<String, String>) {
    Card(modifier = modifier) {
        LazyColumn {
            data.forEach { (key, value) ->
                item {
                    ListItem(
                        colors = SettingsDefaults.colorOnCard(),
                        headlineContent = { Text(text = key, fontWeight = FontWeight.Medium) },
                        supportingContent = { Text(text = value) }
                    )
                }
            }
        }
    }
}
