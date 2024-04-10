package com.lcb.one.ui.screen.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.widget.TextView
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.bean.GithubLatest
import com.lcb.one.network.commonApiService
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.ui.screen.about.UpdateDialog
import com.lcb.one.ui.screen.about.UpdateInfo
import com.lcb.one.ui.service.DownLoadService
import com.lcb.one.ui.service.DownLoadState
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SettingsSimpleText
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DimenUtils
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.android.navigateSingleTop
import com.lcb.one.util.common.JsonUtils
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MoreScreen(navController: NavController) {

    Column {
        // 设置
        SettingsMenuLink(
            title = stringResource(R.string.settings),
            icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.SETTINGS)
        }

        // 项目地址
        val url = stringResource(R.string.project_location_url)
        SettingsMenuLink(
            title = stringResource(R.string.project_location),
            summary = url,
            icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = "") }
        ) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            MyApp.getAppContext().startActivity(intent)
        }

        // 关于
        SettingsMenuLink(
            title = "关于",
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.ABOUT)
        }
    }
}
