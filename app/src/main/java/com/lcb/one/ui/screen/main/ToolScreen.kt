package com.lcb.one.ui.screen.main

import android.app.WallpaperManager
import android.content.Intent
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
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.activity.ClockActivity
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.ui.screen.main.widget.ToolCard
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.android.navigateSingleTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

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
                onClick = { navController.navigateSingleTop(Route.DEVICE) },
                label = { Text(text = Localization.deviceInfo) }
            )
            ElevatedAssistChip(
                onClick = { getWallPaper() },
                label = { Text(text = Localization.extractWallpaper) }
            )

            ElevatedAssistChip(
                onClick = { navController.navigateSingleTop(Route.APPS) },
                label = { Text(text = Localization.appList) }
            )
        }

        // 更多
        ToolCard(
            title = Localization.more,
            icon = { Icon(Icons.Rounded.MiscellaneousServices, "") }
        ) {
            var showClock by remember { mutableStateOf(false) }

            ElevatedAssistChip(
                onClick = { navController.navigateSingleTop(Route.BILI) },
                label = { Text(text = Localization.obtainBilibiliCover) }
            )
            ElevatedAssistChip(
                onClick = { showClock = true },
                label = { Text(text = Localization.clockScreen) }
            )

            if (showClock) {
                LocalContext.current.run {
                    startActivity(Intent(this, ClockActivity::class.java))
                }
                showClock = false
            }

            ElevatedAssistChip(
                onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_ASSISTANT) },
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
    val navController = LocalNav.current!!
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