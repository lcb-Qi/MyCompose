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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.activity.ClockActivity
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.widget.ToolCard
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.player.MusicPlayerScreen
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.screen.qmc.QmcConverterScreen
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.Res
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 设备
        ToolCard(
            title = stringResource(R.string.device),
            icon = { Icon(Icons.Rounded.PhoneAndroid, "") }
        ) {
            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(DeviceInfoScreen) },
                label = { Text(text = DeviceInfoScreen.label) }
            )
            var showPermissionDialog by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            ElevatedAssistChip(
                onClick = {
                    scope.launch {
                        showPermissionDialog = !getWallPaper()
                    }
                },
                label = { Text(text = stringResource(R.string.extract_wallpaper)) }
            )

            SimpleMessageDialog(
                show = showPermissionDialog,
                message = stringResource(R.string.msg_request_all_file_and_image_permission),
                confirmText = stringResource(R.string.go_settings),
                onCancel = { showPermissionDialog = false },
                onConfirm = {
                    showPermissionDialog = false
                    navController.launchSingleTop(PrivacyScreen)
                }
            )

            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(InstalledAppsScreen) },
                label = { Text(text = InstalledAppsScreen.label) }
            )
        }

        // 更多
        ToolCard(
            title = stringResource(R.string.more),
            icon = { Icon(Icons.Rounded.MiscellaneousServices, "") }
        ) {
            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(BiliBiliScreen) },
                label = { Text(text = BiliBiliScreen.label) }
            )
            val context = LocalContext.current
            ElevatedAssistChip(
                onClick = { context.startActivity(Intent(context, ClockActivity::class.java)) },
                label = { Text(text = stringResource(R.string.clock_screen)) }
            )
            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(MenstruationAssistantScreen) },
                label = { Text(text = MenstruationAssistantScreen.label) }
            )

            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(QmcConverterScreen) },
                label = { Text(text = QmcConverterScreen.label) }
            )

            ElevatedAssistChip(
                onClick = { navController.launchSingleTop(MusicPlayerScreen) },
                label = { Text(text = MusicPlayerScreen.label) }
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
        ToastUtils.showToast(Res.string(R.string.save_failed))
    }.onSuccess {
        ToastUtils.showToast("${Res.string(R.string.save_success)} ${it.getOrDefault("")}")
        success = true
    }

    return success
}