package com.lcb.one.ui.screen.applist.widget

import com.lcb.one.ui.screen.applist.repo.AppInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization

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
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        for (appInfo in apps) {
            if (!filterAppType(appInfo) || !filterKeyWord(appInfo)) continue
            item(key = appInfo.packageName) {
                AppInfoCard(appInfo = appInfo, onClick = onItemClick)
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
                    text = String.format(Localization.sumOfApps, count),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}