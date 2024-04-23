package com.lcb.one.ui.screen.main

import android.app.WallpaperManager
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.navigation.NavController
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.activity.ClockActivity
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.android.navigateSingleTop
import com.lcb.one.util.common.ThreadPool

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolScreen() {
    val navController = LocalNav.current!!

    // 设备
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToolBox(
            title = stringResource(R.string.device),
            icon = { Icon(Icons.Rounded.PhoneAndroid, "") }
        ) {
            ElevatedAssistChip(
                onClick = { navController.navigateSingleTop(Route.DEVICE) },
                label = { Text(text = stringResource(R.string.device_info)) }
            )
            ElevatedAssistChip(
                onClick = { getWallPaper() },
                label = { Text(text = stringResource(R.string.extract_wallpaper)) }
            )

            ElevatedAssistChip(
                onClick = { navController.navigateSingleTop(Route.APPS) },
                label = { Text(text = "应用列表") }
            )
        }

        // 其他
        ToolBox(
            title = stringResource(R.string.other),
            icon = { Icon(Icons.Rounded.MiscellaneousServices, "") }
        ) {
            var showClock by remember { mutableStateOf(false) }

            ElevatedAssistChip(
                onClick = { navController.navigateSingleTop(Route.BILI) },
                label = { Text(text = stringResource(R.string.obtain_bilibili_cover)) }
            )
            ElevatedAssistChip(
                onClick = { showClock = true },
                label = { Text(text = stringResource(R.string.clock_screen)) }
            )

            if (showClock) {
                LocalContext.current.run {
                    startActivity(Intent(this, ClockActivity::class.java))
                }
                showClock = false
            }
        }

        DevItems(navController)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DevItems(navController: NavController) {
    if (!AppGlobalConfigs.appDevMode) return
    ToolBox(
        title = "更多",
        icon = { Icon(Icons.Rounded.MiscellaneousServices, "") }
    ) {
        ElevatedAssistChip(
            enabled = false,
            onClick = { },
            label = { Text(text = stringResource(R.string.bv_to_av)) }
        )
        ElevatedAssistChip(
            enabled = false,
            onClick = { },
            label = { Text(text = stringResource(R.string.av_to_bv)) }
        )
        ElevatedAssistChip(
            onClick = { navController.navigateSingleTop(Route.MENSTRUAL_CYCLE_ASSISTANT) },
            label = { Text(text = "经期助手") },
            trailingIcon = {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.icon_beta),
                    contentDescription = ""
                )
            }
        )
    }
}

private fun getWallPaper() {
    ThreadPool.executeOnBackground {
        runCatching {
            val bitmap =
                WallpaperManager.getInstance(MyApp.getAppContext()).drawable?.toBitmap()
            DownLoadUtil.writeBitmapToImageFile(bitmap!!)
        }.onFailure {
            ToastUtils.showToast("保存失败 ${it.message}")
        }.onSuccess { ToastUtils.showToast("保存成功 $it") }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ToolBox(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable FlowRowScope.() -> Unit
) {
    var showDetail by rememberSaveable { mutableStateOf(false) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon()
            Text(text = title, Modifier.weight(1f))
            IconButton(onClick = { showDetail = !showDetail }) {
                Icon(
                    if (showDetail) Icons.Rounded.ArrowDropDown else Icons.AutoMirrored.Rounded.ArrowRight,
                    ""
                )
            }
        }

        AnimatedVisibility(visible = showDetail) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        }
    }
}