package com.lcb.one.ui.page

import android.app.Activity
import android.app.WallpaperManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.FriendlyExitHandler
import com.lcb.one.ui.widget.dialog.CoverGetDialog
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.common.ThreadPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ToolPage(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = RouteConfig.HOME,
        enterTransition = { slideInHorizontally(animationSpec = tween(500)) { it } },
        exitTransition = { slideOutHorizontally(animationSpec = tween(500)) { -it } },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(500)) { -it } },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(500)) { it } },
        modifier = modifier
    ) {
        composable(RouteConfig.HOME) { ToolPageImpl(navController) }
        composable(RouteConfig.DEVICE) { DeviceInfoPage() }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ToolPageImpl(navController: NavController) {
    FriendlyExitHandler()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToolBox(
            title = stringResource(R.string.device),
            icon = { Icon(Icons.Filled.PhoneAndroid, "") }) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ElevatedAssistChip(
                    onClick = { navController.navigateSingleTop(RouteConfig.DEVICE) },
                    label = { Text(text = stringResource(R.string.device_info)) })
                ElevatedAssistChip(onClick = {
                    ThreadPool.executeOnBackground {
                        runCatching {
                            val drawable =
                                WallpaperManager.getInstance(MyApp.getAppContext()).drawable
                            drawable?.toBitmapOrNull()?.let {
                                DownLoadUtil.writeBitmapToImageFile(it)
                            }
                        }.onFailure {
                            ToastUtils.showToast("保存失败 ${it.message}")
                        }
                    }
                }, label = { Text(text = stringResource(R.string.extract_wallpaper)) })
            }
        }

        ToolBox(
            title = stringResource(R.string.other),
            icon = { Icon(Icons.Filled.MiscellaneousServices, "") }) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var showCoverGet by remember { mutableStateOf(false) }
                ElevatedAssistChip(
                    onClick = { showCoverGet = true },
                    label = { Text(text = stringResource(R.string.obtain_bilibili_cover)) })
                ElevatedAssistChip(
                    onClick = { },
                    label = { Text(text = stringResource(R.string.bv_to_av)) })
                ElevatedAssistChip(
                    onClick = { },
                    label = { Text(text = stringResource(R.string.av_to_bv)) })

                if (showCoverGet) {
                    CoverGetDialog(onDismiss = { showCoverGet = false }) { saveCover(it) }
                }
            }
        }
    }
}

@Composable
private fun ToolBox(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit
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
                    if (showDetail) Icons.Filled.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowRight,
                    ""
                )
            }
        }

        if (showDetail) {
            content()
        }
    }
}

private fun saveCover(url: String) {
    CoroutineScope(Dispatchers.IO).launch {
        DownLoadUtil.saveImageFromUrl(url)
    }
}