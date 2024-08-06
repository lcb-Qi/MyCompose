package com.lcb.one.ui.screen.main.tool

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen
import com.lcb.one.ui.screen.main.widget.ToolCard
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.Res
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeviceTool(navController: NavHostController) {
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
                    val drawable = AppUtils.getWallpaper()
                    if (drawable == null) {
                        showPermissionDialog = true
                        return@launch
                    }

                    StorageUtils.createImageFile(drawable.toBitmap(), "wallpaper")
                        .onFailure {
                            ToastUtils.showToast(Res.string(R.string.save_failed))
                            showPermissionDialog = true
                        }.onSuccess {
                            ToastUtils.showToast("${Res.string(R.string.save_success)} $it")
                            showPermissionDialog = false
                        }
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
}