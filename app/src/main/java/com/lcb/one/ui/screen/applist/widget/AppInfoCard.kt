package com.lcb.one.ui.screen.applist.widget

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lcb.one.ui.widget.common.listItemColorOnCard

@Composable
fun AppInfoCard(
    modifier: Modifier = Modifier,
    icon: Drawable,
    label: String,
    appId: String,
    onClick: (appId: String) -> Unit = {}
) {
    val iconContent: @Composable () -> Unit = {
        AsyncImage(
            modifier = Modifier.size(24.dp),
            model = icon,
            contentDescription = label
        )
    }

    val labelContent: @Composable () -> Unit = {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    val appIdContent: @Composable () -> Unit = {
        Text(
            text = appId,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    Card(modifier = modifier, onClick = { onClick(appId) }) {
        ListItem(
            colors = listItemColorOnCard(),
            leadingContent = iconContent,
            headlineContent = labelContent,
            supportingContent = appIdContent,
            trailingContent = {
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
        )
    }
}