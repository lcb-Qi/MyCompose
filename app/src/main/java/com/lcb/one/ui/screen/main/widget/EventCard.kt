package com.lcb.one.ui.screen.main.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun EventCard(modifier: Modifier = Modifier, title: String, content: String, icon: Any? = null) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(icon)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }
    }
}