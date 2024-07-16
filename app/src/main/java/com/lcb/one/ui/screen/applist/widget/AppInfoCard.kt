package com.lcb.one.ui.screen.applist.widget

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
import com.lcb.one.ui.screen.applist.repo.AppInfo
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults

@Composable
fun AppInfoCard(
    modifier: Modifier = Modifier,
    appInfo: AppInfo,
    onClick: (appId: String) -> Unit = {}
) {
    val iconContent: @Composable () -> Unit = {
        AsyncImage(
            modifier = Modifier.size(36.dp),
            model = appInfo.icon,
            contentDescription = appInfo.label
        )
    }

    val labelContent: @Composable () -> Unit = {
        Text(
            text = appInfo.label,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    val appIdContent: @Composable () -> Unit = {
        Text(
            text = appInfo.packageName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    Card(modifier = modifier, onClick = { onClick(appInfo.packageName) }) {
        ListItem(
            colors = SettingsDefaults.colorOnCard(),
            leadingContent = iconContent,
            headlineContent = labelContent,
            supportingContent = appIdContent,
            trailingContent = {
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
        )
    }
}