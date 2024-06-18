package com.lcb.one.ui.screen.main.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lcb.one.ui.widget.common.listItemColorOnCard

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    icon: Any? = null,
    onClick: () -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        ListItem(
            colors = listItemColorOnCard(),
            headlineContent = { Text(text = title, fontWeight = FontWeight.Medium) },
            supportingContent = { Text(text = content) },
            trailingContent = {
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
        )
    }
}