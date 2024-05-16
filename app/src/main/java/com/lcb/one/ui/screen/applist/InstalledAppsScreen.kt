package com.lcb.one.ui.screen.applist

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.ANIMATE_DURATION
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.applist.widget.AppInfo
import com.lcb.one.ui.screen.applist.widget.AppType
import com.lcb.one.ui.screen.applist.widget.ApplicationInfoDialog
import com.lcb.one.ui.screen.applist.widget.InstalledAppList
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.Locale
import kotlin.system.measureTimeMillis

@Composable
fun InstalledAppsScreen() {
    Scaffold(topBar = { ToolBar(title = Localization.appList) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var loadSuccess by remember { mutableStateOf(false) }
            var allApps by remember { mutableStateOf(emptyList<AppInfo>()) }

            LaunchedEffect(Unit) {
                if (loadSuccess) return@LaunchedEffect
                delay(ANIMATE_DURATION.toLong())
                withContext(Dispatchers.IO) {
                    val measureTime = measureTimeMillis {
                        allApps = loadInstalledApps()
                    }
                    loadSuccess = true
                    val msg =
                        String.format(Localization.loadSuccessMsg, allApps.size, measureTime)
                    ToastUtils.showToast(msg)
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

            if (!loadSuccess) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                InstalledAppList(apps = allApps, displayType = AppType.from(currentIndex)) {
                    packageName = it
                    showDetail = true
                }
            }

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
