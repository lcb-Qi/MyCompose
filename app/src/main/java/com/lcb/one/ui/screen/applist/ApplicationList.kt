package com.lcb.one.ui.screen.applist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lcb.one.util.android.AppUtils

// TODO: 性能优化、应用信息、提取图标/apk

enum class AppSelection(val label: String) {
    USER("用户应用"), SYSTEM("系统应用"), ALL("所有应用")
}

@Composable
fun ApplicationList(modifier: Modifier = Modifier, appSelection: AppSelection) {
    val packageManager = LocalContext.current.packageManager

    val apps = when (appSelection) {
        AppSelection.USER -> AppUtils.getUserApps(LocalContext.current)
        AppSelection.SYSTEM -> AppUtils.getSystemApps(LocalContext.current)
        AppSelection.ALL -> AppUtils.getAllApps(LocalContext.current)
    }
    LazyColumn(modifier = modifier) {
        items(count = apps.size, key = { apps[it] }) {
            val packageName = apps[it]
            ListItem(
                modifier = Modifier.clickable {},
                leadingContent = {
                    AsyncImage(
                        modifier = Modifier.size(40.dp),
                        model = packageManager.getApplicationIcon(packageName),
                        contentDescription = ""
                    )
                },
                headlineContent = {
                    Text(
                        text = packageManager.getApplicationLabel(
                            packageManager.getApplicationInfo(
                                packageName,
                                0
                            )
                        )
                            .toString(),
                        fontWeight = FontWeight.Medium
                    )
                },
                supportingContent = { Text(text = packageName) }
            )
        }
    }
}