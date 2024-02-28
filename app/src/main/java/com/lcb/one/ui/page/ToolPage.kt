package com.lcb.one.ui.page

import android.app.WallpaperManager
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.ToolButton
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.common.ThreadPool

@Composable
fun ToolPage(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = RouteConfig.HOME,
        enterTransition = { slideInHorizontally(animationSpec = tween(500)) { it } },
        exitTransition = { slideOutHorizontally(animationSpec = tween(500)) { -it } },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(500)) { -it } },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(500)) { it } },
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        composable(RouteConfig.HOME) { ToolPageImpl(navController) }
        composable(RouteConfig.DEVICE) { DeviceInfoPage() }
        composable(RouteConfig.BILI) { BiliPage() }
    }
}

@Composable
private fun ToolPageImpl(navController: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        addToolButton("设备信息", true) {
            navController.navigateSingleTop(RouteConfig.DEVICE)
        }
        addToolButton("提取壁纸") {
            ThreadPool.executeOnBackground {
                runCatching {
                    val drawable = WallpaperManager.getInstance(MyApp.getAppContext()).drawable
                    drawable?.toBitmapOrNull()?.let {
                        DownLoadUtil.writeBitmapToImageFile(it)
                    }
                }.onFailure {
                    ToastUtils.showToast("保存失败 ${it.message}")
                }
            }
        }
        addToolButton("bilibili工具") {
            navController.navigateSingleTop(RouteConfig.BILI)
        }
    }
}

private fun LazyGridScope.addToolButton(
    title: String = "",
    singleLine: Boolean = false,
    action: (() -> Unit)? = null
) {
    if (singleLine) {
        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            ToolButton(title = title) { action?.invoke() }
        }
    } else {
        item {
            ToolButton(title = title) { action?.invoke() }
        }
    }
}