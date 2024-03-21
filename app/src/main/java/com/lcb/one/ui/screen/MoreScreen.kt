package com.lcb.one.ui.screen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.navigation.navOptions
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.bean.GithubLatest
import com.lcb.one.network.os.RESTFulRequest
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.ui.service.DownLoadService
import com.lcb.one.ui.service.DownLoadState
import com.lcb.one.ui.widget.FriendlyExitHandler
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SettingsSimpleText
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.PACKAGE_ME
import com.lcb.one.util.android.ToastUtils
import io.noties.markwon.Markwon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavController.navigateSingleTop(route: String) {
    navigate(route = route, navOptions = navOptions { launchSingleTop = true })
}

@Composable
fun MoreScreen(navController: NavController) {
    FriendlyExitHandler()

    Column {
        // 设置
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.settings)) },
            icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.SETTINGS)
        }

        // 项目地址
        val url = stringResource(R.string.project_location_url)
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.project_location)) },
            summary = { SettingsSummary(url) },
            icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = "") }
        ) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            MyApp.getAppContext().startActivity(intent)
        }

        var showUpdate by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var md by remember { mutableStateOf("") }
        var newVersion by remember { mutableStateOf("") }
        var fileName by remember { mutableStateOf("") }
        var downloadUrl by remember { mutableStateOf("") }
        val versionName = BuildConfig.VERSION_NAME
        val buildTime = stringResource(R.string.BUILD_TIME)
        val versionInfo = "$versionName($buildTime)"
        SettingsSimpleText(
            title = { SettingsTitle(stringResource(R.string.version_info)) },
            summary = { SettingsSummary(versionInfo) },
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
            scope.launch {
                val updateInfo = checkUpdate()
                if (updateInfo == null) {
                    ToastUtils.showToast("未检测到新版本")
                    return@launch
                }
                if (updateInfo.version == BuildConfig.VERSION_NAME) {
                    ToastUtils.showToast("已是最新版")
                    return@launch
                }
                showUpdate = true
            }
        }

        if (showUpdate) {
            var state by remember { mutableStateOf(DownLoadState.IDLE) }
            var enable by remember { mutableStateOf(true) }
            var text by remember { mutableStateOf("下载") }
            var apkUri: Uri? by remember { mutableStateOf(null) }

            var stub: DownLoadService.Stub? = null
            val serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    stub = service as DownLoadService.Stub
                    stub?.addDownloadStateListener { downLoadState, uri ->
                        state = downLoadState
                        enable = when (state) {
                            DownLoadState.IDLE -> true
                            DownLoadState.DOWNLOADING -> false
                            DownLoadState.SUCCESS -> true
                            DownLoadState.FAILED -> true
                        }
                        text = when (state) {
                            DownLoadState.IDLE -> "下载"
                            DownLoadState.DOWNLOADING -> "下载中"
                            DownLoadState.SUCCESS -> "安装"
                            DownLoadState.FAILED -> "下载"
                        }
                        apkUri = uri
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                }
            }
            context.bindService(
                Intent(context, DownLoadService::class.java), serviceConnection,
                Context.BIND_AUTO_CREATE
            )


            val markdown = Markwon.builder(context).build()
            AlertDialog(
                onDismissRequest = { },
                dismissButton = {
                    TextButton(onClick = {
                        stub?.cancel()
                        context.unbindService(serviceConnection)
                        showUpdate = false
                    }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (state == DownLoadState.SUCCESS) {
                                AppUtils.installApk(context, apkUri!!)
                            } else {
                                stub?.start(downloadUrl, fileName)
                            }
                        },
                        enabled = enable
                    ) {
                        Text(text = text)
                    }
                },
                text = {
                    AndroidView(
                        factory = {
                            val textView = TextView(it)
                            markdown.setMarkdown(textView, md)

                            return@AndroidView textView
                        },
                        update = {
                            markdown.setMarkdown(it, md)
                        }
                    )
                }
            )
        }
    }
}

data class UpdateInfo(
    val version: String,
    val url: String,
    val message: String,
    val filename: String
)

private suspend fun checkUpdate(): UpdateInfo? = runCatching {
    withContext(Dispatchers.IO) {
        var updateInfo: UpdateInfo? = null
        RESTFulRequest.newBuilder("https://api.github.com/repos/lcb-Qi/MyCompose/releases/latest")
            .build()
            .getResult(GithubLatest::class.java)
            .onSuccess {
                if (it == null) return@onSuccess
                updateInfo = UpdateInfo(
                    it.tagName,
                    it.assets[0].browserDownloadUrl,
                    it.body,
                    it.assets[0].name
                )
            }

        return@withContext updateInfo
    }
}.getOrNull()
