package com.lcb.one.ui.screen.about

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Screen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.about.repo.model.AppVersion
import com.lcb.one.ui.screen.about.repo.UpdateAccessor
import com.lcb.one.ui.screen.about.repo.model.UpdateInfo
import com.lcb.one.ui.screen.about.widget.AboutSaltFishDialog
import com.lcb.one.ui.screen.about.widget.UpdateInfoDialog
import com.lcb.one.ui.screen.webview.WebScreen
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

object AboutScreen : Screen {
    override val route: String
        get() = "About"

    @Composable
    override fun Content(args: Bundle?) {
        val navController = LocalNav.current!!
        Scaffold(topBar = { ToolBar(title = "${Localization.about}${Localization.appName}") }) { innerPadding ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // 版本信息
                var showDetail by remember { mutableStateOf(false) }
                ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                    SimpleSettingsMenuLink(modifier = Modifier.padding(top = 8.dp),
                        title = Localization.versionInfo,
                        summary = BuildConfig.VERSION_NAME,
                        icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") },
                        onClick = { showDetail = true })

                    AboutSaltFishDialog(showDetail) { showDetail = false }

                    // 检查更新
                    var showUpdate by remember { mutableStateOf(false) }
                    var updateInfo: UpdateInfo? by remember { mutableStateOf(null) }
                    LaunchedEffect(Unit) {
                        updateInfo = UpdateAccessor.getLastRelease()
                    }
                    val scope = rememberCoroutineScope()
                    SimpleSettingsMenuLink(title = Localization.checkUpdates,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Update,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            scope.launch {
                                updateInfo = UpdateAccessor.getLastRelease()
                                if (updateInfo == null) {
                                    ToastUtils.showToast(Localization.noNewVersion)
                                    return@launch
                                }
                                if (!BuildConfig.DEBUG && updateInfo!!.version <= AppVersion.current) {
                                    ToastUtils.showToast(Localization.alreadyLast)
                                    return@launch
                                }
                                showUpdate = true
                            }
                        })

                    UpdateInfoDialog(
                        show = showUpdate && updateInfo != null,
                        updateInfo = updateInfo
                    ) {
                        showUpdate = false
                    }

                    // 项目地址
                    val url = stringResource(R.string.project_location_url)
                    val launchBrowser: () -> Unit = {
                        if (AppGlobalConfigs.useBuiltinBrowser) {
                            navController.launchSingleTop(WebScreen.createRoute(url))
                        } else {
                            AppUtils.launchSystemBrowser(uri = url.toUri())
                        }
                    }
                    SimpleSettingsMenuLink(
                        modifier = Modifier.padding(bottom = 8.dp),
                        title = Localization.projectUrl,
                        summary = url,
                        icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = "") },
                        onClick = launchBrowser
                    )
                }
            }
        }
    }
}