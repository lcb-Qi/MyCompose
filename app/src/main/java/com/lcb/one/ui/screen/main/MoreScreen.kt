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
import com.lcb.one.network.os.RESTFulRequest
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.ui.service.DownLoadService
import com.lcb.one.ui.service.DownLoadState
import com.lcb.one.ui.widget.common.FriendlyExitHandler
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SettingsSimpleText
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.android.navigateSingleTop
import io.noties.markwon.Markwon
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

        var showUpdate by remember { mutableStateOf(false) }
        var updateInfo: UpdateInfo? by remember { mutableStateOf(null) }
        val scope = rememberCoroutineScope()
        val versionName = BuildConfig.VERSION_NAME
        val buildTime = stringResource(R.string.BUILD_TIME)
        val versionInfo = "$versionName($buildTime)"
        SettingsSimpleText(
            title = stringResource(R.string.version_info),
            summary = versionInfo,
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
            scope.launch {
                updateInfo = checkUpdate()
                if (updateInfo == null) {
                    ToastUtils.showToast("未检测到新版本")
                    return@launch
                }
                if (compareVersion(updateInfo!!.version, BuildConfig.VERSION_NAME) <= 0) {
                    ToastUtils.showToast("已是最新版")
                    return@launch
                }
                showUpdate = true
            }
        }

        UpdateDialog(showUpdate, updateInfo) { showUpdate = false }
    }
}

/**
 * Compare version
 *
 * @return 1: version1 > version12; -1: version1 < version2; 0: version1 == version2
 */
private fun compareVersion(version1: String, version2: String): Int {
    val v1 = version1.split(".").map { it.toInt() }
    val v2 = version2.split(".").map { it.toInt() }

    require(v1.size == 3 && v2.size == 3) { "version must like x.x.x" }

    v1.forEachIndexed { index, i ->
        if (i > v2[index]) {
            return 1
        } else if (i < v2[index]) {
            return -1
        }
    }

    return 0
}

@Composable
fun UpdateDialog(show: Boolean, updateInfo: UpdateInfo?, onCancel: () -> Unit) {
    if (!show || updateInfo == null) return

    val context = LocalContext.current

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
        title = { Text(text = updateInfo.version, style = MaterialTheme.typography.titleLarge) },
        text = {
            AndroidView(
                factory = {
                    val textView = TextView(it)
                    markdown.setMarkdown(textView, updateInfo.message)
                    return@AndroidView textView
                },
                update = {
                    markdown.setMarkdown(it, updateInfo.message)
                }
            )
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    if (state == DownLoadState.SUCCESS) {
                        AppUtils.installApk(context, apkUri!!)
                    } else {
                        stub?.start(updateInfo.url, updateInfo.filename)
                    }
                },
                enabled = enable
            ) {
                Text(text = text)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    stub?.cancel()
                    context.unbindService(serviceConnection)
                    onCancel()
                }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Keep
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
