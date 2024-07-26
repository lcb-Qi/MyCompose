package com.lcb.one.ui.screen.main

import android.app.WallpaperManager
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Transform
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.lcb.one.localization.Localization
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.activity.ClockActivity
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.player.MusicPlayerScreen
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.widget.ToolCard
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.screen.qmc.QmcConverterScreen
import com.lcb.one.ui.screen.zxing.QrCodeScreen
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolScreen() {
    val navController = LocalNav.current!!
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
                onClick = { navController.launchSingleTop(DeviceInfoScreen) },
                label = { Text(text = Localization.deviceInfo) }
            )
            var showPermissionDialog by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            ElevatedAssistChip(
                onClick = {
                    scope.launch {
                        showPermissionDialog = !getWallPaper()
                    }
                },
                label = { Text(text = Localization.extractWallpaper) }
            )

            SimpleMessageDialog(
                show = showPermissionDialog,
                message = "请开启“所有文件访问”和“读取照片和视频”权限",
                confirmText = Localization.goSettings,
                onCancel = { showPermissionDialog = false },
                onConfirm = {
                    showPermissionDialog = false
                    navController.launchSingleTop(PrivacyScreen)
                }
            )

            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(InstalledAppsScreen) },
                label = { Text(text = Localization.appList) }
            )
        }

        // 转换工具
        ToolCard(title = "转换类", icon = { Icon(Icons.Rounded.Transform, null) }) {
            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(QmcConverterScreen) },
                label = { Text(text = "Qmc 转换器（QQ 音乐解密）") }
            )
        }

        // 更多
        ToolCard(
            title = Localization.more,
            icon = { Icon(Icons.Rounded.MiscellaneousServices, "") }
        ) {
            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(BiliBiliScreen) },
                label = { Text(text = Localization.obtainBilibiliCover) }
            )
            val context = LocalContext.current
            ElevatedAssistChip(
                onClick = { context.startActivity(Intent(context, ClockActivity::class.java)) },
                label = { Text(text = Localization.clockScreen) }
            )
            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(MenstruationAssistantScreen) },
                label = { Text(text = Localization.menstrualAssistant) }
            )

            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(MusicPlayerScreen) },
                label = { Text(text = "音乐播放器") }
            )

            ElevatedAssistChip(
                enabled = false,
                onClick = { navController.launchSingleTop(QrCodeScreen) },
                label = { Text(text = "二维码工具") }
            )

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
}

private suspend fun getWallPaper(): Boolean {
    var success = false
    runCatching {
        val drawable = WallpaperManager.getInstance(MyApp.get()).drawable
        StorageUtils.createImageFile(drawable!!.toBitmap(), "wallpaper")
    }.onFailure {
        ToastUtils.showToast(Localization.saveFailed)
    }.onSuccess {
        ToastUtils.showToast("${Localization.saveSuccess} ${it.getOrDefault("")}")
        success = true
    }

    return success
}