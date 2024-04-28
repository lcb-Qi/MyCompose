package com.lcb.one.ui.screen.applist

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.applist.widget.AppInfo
import com.lcb.one.ui.screen.applist.widget.AppType
import com.lcb.one.ui.screen.applist.widget.ApplicationInfoDialog
import com.lcb.one.ui.screen.applist.widget.InstalledAppList
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.dialog.LoadingDialog
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.Locale
import kotlin.system.measureTimeMillis

@Composable
fun InstalledAppsScreen() {
    Scaffold(topBar = { ToolBar(title = "应用列表") }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var loadSuccess by remember { mutableStateOf(false) }
            var allApps by remember { mutableStateOf(emptyList<AppInfo>()) }
            var userAppCount by remember { mutableIntStateOf(0) }

            LaunchedEffect(Unit) {
                if (loadSuccess) return@LaunchedEffect
                withContext(Dispatchers.IO) {
                    val measureTime = measureTimeMillis {
                        allApps = loadInstalledApps()
                        userAppCount = allApps.count { !it.isSystemApp }
                    }
                    loadSuccess = true
                    ToastUtils.showToast("加载完成，总计${allApps.size}个应用，耗时${measureTime}ms")
                }
            }


            var currentIndex by remember { mutableIntStateOf(0) }
            TabRow(currentIndex, modifier = Modifier.padding(horizontal = 16.dp)) {
                AppType.entries.forEachIndexed { index, item ->
                    Tab(selected = index == currentIndex,
                        onClick = { currentIndex = index },
                        text = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }
            }

            var showDetail by remember { mutableStateOf(false) }
            var packageName by remember { mutableStateOf("") }
            InstalledAppList(apps = allApps, displayType = AppType.from(currentIndex)) {
                packageName = it
                showDetail = true
            }

            LoadingDialog(show = !loadSuccess)
            ApplicationInfoDialog(showDetail, packageName) { showDetail = false }
        }
    }
}

private suspend fun loadInstalledApps() = withContext(Dispatchers.IO) {
    val collator = Collator.getInstance(Locale.getDefault()).apply {
        strength = Collator.PRIMARY
        decomposition = Collator.CANONICAL_DECOMPOSITION
    }

    val pm = MyApp.getAppContext().packageManager

    pm.getInstalledPackages(0).asSequence()
        .map {
            val packageName = it.packageName
            val label = it.applicationInfo.loadLabel(pm)
            val icon = it.applicationInfo.loadIcon(pm)
            val isSystemApp = it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0

            AppInfo(packageName, label.toString(), icon, isSystemApp)
        }
        .sortedWith { o1, o2 ->
            collator.compare(o1.label, o2.label)
        }
        .toList()
}
