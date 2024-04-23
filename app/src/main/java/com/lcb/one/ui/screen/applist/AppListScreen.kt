package com.lcb.one.ui.screen.applist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lcb.one.ui.screen.applist.widget.ToolButton
import com.lcb.one.ui.screen.applist.widget.AppType
import com.lcb.one.ui.screen.applist.widget.ApplicationList
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.common.launchIOSafely
import kotlinx.coroutines.launch

@Composable
fun AppListScreen() {
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
            ApplicationList(displayType = AppType.from(currentIndex)) {
                packageName = it
                showDetail = true
            }

            ApplicationInfoDialog(showDetail, packageName) { showDetail = false }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationInfoDialog(show: Boolean, packageName: String, onDisMiss: () -> Unit) {
    if (!show || packageName.isBlank()) return

    val pm = LocalContext.current.packageManager
    val packageInfo = pm.getPackageInfo(packageName, 0)
    val label = packageInfo.applicationInfo.loadLabel(pm).toString()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(onDismissRequest = onDisMiss) {
        Column(
            Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.size(50.dp),
                    model = packageInfo.applicationInfo.loadIcon(pm),
                    contentDescription = ""
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            ToolButton(
                text = "提取桌面图标",
                leadingIcon = Icons.Rounded.Image,
                onclick = {
                    scope.launchIOSafely {
                        val icon = AppUtils.getAppIcon(packageName = packageName)
                        if (icon != null) {
                            DownLoadUtil.writeBitmapToImageFile(icon, "${label}_logo")
                                .onSuccess { ToastUtils.showToast("保存成功 $it") }
                                .onFailure { ToastUtils.showToast("保存失败") }
                        } else {
                            ToastUtils.showToast("保存失败")
                        }
                    }
                }
            )

            ToolButton(
                text = "提取安装包",
                leadingIcon = Icons.Rounded.Android,
                onclick = {
                    scope.launchIOSafely {
                        DownLoadUtil.saveApk(AppUtils.getApkPath(packageName), label)
                            .onSuccess { ToastUtils.showToast("保存成功 $it") }
                            .onFailure { ToastUtils.showToast("保存失败") }
                    }
                }
            )
        }
    }
}
