package com.lcb.one.ui.page

import android.app.WallpaperManager
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.ToolButton
import com.lcb.one.util.DownLoadUtil
import com.lcb.one.util.ThreadPool

@Composable
fun ToolPage(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = RouteConfig.HOME,
        enterTransition = { slideInVertically(animationSpec = tween(500)) { it } },
        exitTransition = { slideOutVertically(animationSpec = tween(500)) { -it } },
        modifier = modifier
    ) {
        composable(RouteConfig.HOME) { ToolPageImpl(navController) }
        composable(RouteConfig.DEVICE) { DeviceInfoPage() }
        composable(RouteConfig.BILI) { BiliPage() }
    }
}

@Composable
fun ToolPageImpl(navController: NavController? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolView(Modifier.weight(1f), navController)
    }
}

@Composable
private fun ToolView(modifier: Modifier, navController: NavController? = null) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(2)) {
        addToolButton("设备信息", true) {
            navController?.navigate(RouteConfig.DEVICE)
        }
        addToolButton("提取壁纸") {
            ThreadPool.executeOnBackground {
                val drawable = WallpaperManager.getInstance(MyApp.getAppContext()).drawable
                drawable?.toBitmapOrNull()?.let {
                    DownLoadUtil.writeBitmapToImageFile(it)
                }
            }
        }
        addToolButton("bilibili工具") {
            navController?.navigate(RouteConfig.BILI)
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