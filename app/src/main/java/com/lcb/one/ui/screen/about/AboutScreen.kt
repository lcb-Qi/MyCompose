package com.lcb.one.ui.screen.about

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.about.repo.model.AppVersion
import com.lcb.one.ui.screen.about.repo.UpdateAccessor
import com.lcb.one.ui.screen.about.repo.model.UpdateInfo
import com.lcb.one.ui.screen.about.widget.UpdateInfoDialog
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SettingsSimpleText
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = { ToolBar(title = "关于${stringResource(R.string.app_name)}") }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            // 版本信息
            val versionName = BuildConfig.VERSION_NAME
            val buildTime = stringResource(R.string.BUILD_TIME)
            val versionInfo = "$versionName($buildTime)"

            var clickCount by remember { mutableIntStateOf(0) }
            LaunchedEffect(clickCount) {
                delay(1000)
                clickCount = 0
            }
            SettingsSimpleText(
                title = stringResource(R.string.version_info),
                summary = versionInfo,
                icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
            ) {
                clickCount++
                if (clickCount >= AppGlobalConfigs.COUNT_TO_ENABLE_DEV_MODE) {
                    AppGlobalConfigs.appDevMode = !AppGlobalConfigs.appDevMode
                    val action = if (AppGlobalConfigs.appDevMode) "已处于" else "已退出"
                    ToastUtils.showToast("${action}开发者模式")
                    clickCount = 0
                }
            }


            // 检查更新
            var showUpdate by remember { mutableStateOf(false) }
            var updateInfo: UpdateInfo? by remember { mutableStateOf(null) }
            LaunchedEffect(Unit) {
                updateInfo = UpdateAccessor.getLastRelease()
            }
            val scope = rememberCoroutineScope()
            SettingsMenuLink(
                title = "检查更新",
                icon = { Icon(imageVector = Icons.Rounded.Update, contentDescription = "") }
            ) {
                scope.launch {
                    updateInfo = UpdateAccessor.getLastRelease()
                    if (updateInfo == null) {
                        ToastUtils.showToast("未检测到新版本")
                        return@launch
                    }
                    if (!BuildConfig.DEBUG && updateInfo!!.version <= AppVersion.current) {
                        ToastUtils.showToast("已是最新版")
                        return@launch
                    }
                    showUpdate = true
                }
            }

            UpdateInfoDialog(show = showUpdate && updateInfo != null, updateInfo = updateInfo) {
                showUpdate = false
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
        }
    }
}