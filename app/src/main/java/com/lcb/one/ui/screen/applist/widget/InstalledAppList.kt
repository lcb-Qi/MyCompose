package com.lcb.one.ui.screen.applist.widget

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val isSystemApp: Boolean
)

enum class AppType(val label: String) {
    USER("用户应用"), SYSTEM("系统应用"), ALL("所有应用");

    companion object {
        fun from(id: Int) = AppType.entries.first { it.ordinal == id }
    }
}

@Composable
fun InstalledAppList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    apps: List<AppInfo>,
    displayType: AppType,
    filterText: String = "",
    onItemClick: (String) -> Unit = {}
) {
    val userAppCount = remember(apps) { apps.count { !it.isSystemApp } }

    val filterAppType: (AppInfo) -> Boolean = {
        (it.isSystemApp && displayType != AppType.USER) || (!it.isSystemApp && displayType != AppType.SYSTEM)
    }

    val filterKeyWord: (AppInfo) -> Boolean = {
        filterText.isBlank()
                || it.packageName.contains(filterText, true)
                || it.label.contains(filterText, true)
    }

    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(count = apps.size, key = { apps[it].packageName }) {
            val appInfo = apps[it]
            if (filterAppType(appInfo) && filterKeyWord(appInfo)) {
                ListItem(
                    modifier = Modifier.clickable { onItemClick(appInfo.packageName) },
                    leadingContent = {
                        AsyncImage(
                            modifier = Modifier.size(24.dp),
                            model = appInfo.icon,
                            contentDescription = ""
                        )
                    },
                    headlineContent = {
                        Text(
                            text = appInfo.label,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    supportingContent = {
                        Text(
                            text = appInfo.packageName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = "")
                    }
                )
            }
        }

        if (apps.isNotEmpty()) {
            item {
                val count = when (displayType) {
                    AppType.USER -> userAppCount
                    AppType.SYSTEM -> apps.size - userAppCount
                    AppType.ALL -> apps.size
                }
                Text(
                    text = "共 $count 个应用",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                )
            }
        }
    }
}