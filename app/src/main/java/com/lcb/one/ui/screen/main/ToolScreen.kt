package com.lcb.one.ui.screen.main

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.lcb.one.localization.Localization
import com.lcb.one.route.NavGraphs
import com.lcb.one.route.destinations.BiliBiliScreenDestination
import com.lcb.one.route.destinations.ClockActivityDestination
import com.lcb.one.route.destinations.DeviceInfoScreenDestination
import com.lcb.one.route.destinations.InstalledAppsScreenDestination
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.navController
import com.lcb.one.ui.screen.main.widget.ToolCard
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.PermissionUtils
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolScreen() {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 设备
        ToolCard(
            title = Localization.device,
            icon = { Icon(Icons.Rounded.PhoneAndroid, "") }
        ) {
            ElevatedAssistChip(
                onClick = { navController.navigate(DeviceInfoScreenDestination) },
                label = { Text(text = Localization.deviceInfo) }
            )
            var showImgDialog by remember { mutableStateOf(false) }
            var showAllFileDialog by remember { mutableStateOf(false) }
            val activity = LocalContext.current as Activity
            ElevatedAssistChip(
                onClick = {
                    if (!PermissionUtils.hasAllFileAccess()) {
                        showAllFileDialog = true
                        return@ElevatedAssistChip
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (PermissionUtils.shouldShowRationale(activity, Manifest.permission.READ_MEDIA_IMAGES)) {
                            showImgDialog = true
                            return@ElevatedAssistChip
                        }

                        if (!PermissionUtils.hasPermission(permission = Manifest.permission.READ_MEDIA_IMAGES)) {
                            PermissionUtils.requestPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
                            return@ElevatedAssistChip
                        }
                    }

                    getWallPaper()
                },
                label = { Text(text = Localization.extractWallpaper) }
            )

            SimpleMessageDialog(
                show = showAllFileDialog,
                title = "请开启“所有文件访问”权限",
                message = "提取桌面壁纸需要使用访问所有文件权限",
                confirmText = Localization.goSettings,
                onCancel = { showAllFileDialog = false },
                onConfirm = {
                    showAllFileDialog = false
                    MyApp.getAppContext().startActivity(AppUtils.getAllFileAccessIntent())
                }
            )

            SimpleMessageDialog(
                show = showImgDialog,
                title = "请开启“读取照片和视频”权限",
                message = "提取桌面壁纸需要使用读取照片和视频权限",
                confirmText = Localization.goSettings,
                onCancel = { showImgDialog = false },
                onConfirm = {
                    showImgDialog = false
                    MyApp.getAppContext().startActivity(AppUtils.getAppDetailSettingsIntent())
                }
            )

            ElevatedAssistChip(
                onClick = { navController.navigate(InstalledAppsScreenDestination) },
                label = { Text(text = Localization.appList) }
            )
        }

        // 更多
        ToolCard(
            title = Localization.more,
            icon = { Icon(Icons.Rounded.MiscellaneousServices, "") }
        ) {
            ElevatedAssistChip(
                onClick = { navController.navigate(BiliBiliScreenDestination) },
                label = { Text(text = Localization.obtainBilibiliCover) }
            )
            ElevatedAssistChip(
                onClick = { navController.navigate(ClockActivityDestination) },
                label = { Text(text = Localization.clockScreen) }
            )
            ElevatedAssistChip(
                onClick = { navController.navigate(NavGraphs.menstruationAssistant) },
                label = { Text(text = Localization.menstrualAssistant) }
            )
        }

        DevItems()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DevItems() {
    if (!AppGlobalConfigs.appDevMode) return
    ToolCard(
        title = "On going",
        icon = { Icon(Icons.Rounded.DeviceUnknown, "") }
    ) {
        ElevatedAssistChip(
            enabled = false,
            onClick = { },
            label = { Text(text = Localization.bv2av) }
        )
        ElevatedAssistChip(
            enabled = false,
            onClick = { },
            label = { Text(text = Localization.av2bv) }
        )
    }
}

private fun getWallPaper() {
    CoroutineScope(EmptyCoroutineContext).launch {
        runCatching {
            val bitmap =
                WallpaperManager.getInstance(MyApp.getAppContext()).drawable?.toBitmap()
            DownLoadUtil.writeBitmapToImageFile(bitmap!!)
        }.onFailure {
            ToastUtils.showToast(Localization.saveFailed)
        }.onSuccess { ToastUtils.showToast("${Localization.saveSuccess} ${it.getOrDefault("")}") }
    }
}