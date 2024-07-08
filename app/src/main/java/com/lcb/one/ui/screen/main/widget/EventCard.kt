package com.lcb.one.ui.screen.main.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    icon: @Composable () -> Unit = { Icon(Icons.Rounded.Event, null) },
    bottomContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(modifier = Modifier.size(32.dp), propagateMinConstraints = true) {
                icon()
            }

            Text(text = title, style = MaterialTheme.typography.titleMedium)

            Text(text = content, style = MaterialTheme.typography.bodyMedium)

            bottomContent?.let {
                Box(content = { it() })
            }
        }
    }
}