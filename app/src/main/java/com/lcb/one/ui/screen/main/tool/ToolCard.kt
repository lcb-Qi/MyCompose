package com.lcb.one.ui.screen.main.tool

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolCard(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable FlowRowScope.() -> Unit
) {
    var expand by rememberSaveable { mutableStateOf(true) }
    Card(onClick = { expand = !expand }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            ListItem(
                colors = SettingsDefaults.colorOnCard(),
                headlineContent = { Text(text = title, fontWeight = FontWeight.Medium) },
                leadingContent = { icon() },
                trailingContent = {
                    Icon(
                        imageVector = if (expand) Icons.Rounded.ArrowDropDown else Icons.AutoMirrored.Rounded.ArrowRight,
                        contentDescription = null
                    )
                }
            )

            AnimatedVisibility(visible = expand) {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = { content() }
                )
            }
        }
    }
}