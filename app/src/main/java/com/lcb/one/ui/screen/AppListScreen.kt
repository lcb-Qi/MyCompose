package com.lcb.one.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lcb.one.ui.widget.AppBar
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.LLog

// TODO: 性能优化、应用信息、提取图标/apk

enum class AppSelection(val label: String) {
    USER("用户应用"), SYSTEM("系统应用"), ALL("所有应用")
}

@Composable
fun AppListScreen() {
    Scaffold(topBar = { AppBar(title = "应用列表") }) { paddingValues ->
        var appSelection by remember { mutableStateOf(AppSelection.USER) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                var showDetail by remember { mutableStateOf(false) }
                TextButton(onClick = { showDetail = true }) {
                    Text(text = appSelection.label)
                }
                DropdownMenu(expanded = showDetail, onDismissRequest = { showDetail = false }) {
                    AppSelection.values().forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.label) },
                            onClick = {
                                showDetail = false
                                appSelection = it
                            }
                        )
                    }
                }
            }
            AppList(appSelection = appSelection)
        }
    }
}

@Composable
private fun AppList(modifier: Modifier = Modifier, appSelection: AppSelection) {
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
