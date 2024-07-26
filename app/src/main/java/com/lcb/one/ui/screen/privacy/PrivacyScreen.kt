package com.lcb.one.ui.screen.privacy

import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.ui.Screen
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink
import com.lcb.one.util.android.AppUtils

object PrivacyScreen : Screen {
    override val route: String
        get() = "Privacy"

    @Composable
    override fun Content(args: Bundle?) {
        val privacyViewModel = viewModel<PrivacyViewModel>()
        val privacy by privacyViewModel.privacyState.collectAsState()

        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                privacyViewModel.checkPermission()
            }
        }

        val activityLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                privacyViewModel.checkPermission()
            }

        Scaffold(topBar = { ToolBar(title = "权限管理") }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                        Card {
                            SimpleSettingsMenuLink(
                                title = "访问照片和视频",
                                summary = "用于提取桌面壁纸",
                                action = { PrivacyAction(hasPermission = privacy.canReadImage) },
                                onClick = { activityLauncher.launch(AppUtils.getAppDetailSettingsIntent()) }
                            )
                        }

                        Card {
                            SimpleSettingsMenuLink(
                                title = "访问全部文件",
                                summary = "用于提取桌面壁纸",
                                action = { PrivacyAction(hasPermission = privacy.canAccessAllFile) },
                                onClick = { activityLauncher.launch(AppUtils.getAllFileAccessIntent()) }
                            )
                        }

                        Card {
                            SimpleSettingsMenuLink(
                                title = "访问音乐和音频",
                                summary = "用于读取本地音乐文件",
                                action = { PrivacyAction(hasPermission = privacy.canReadAudio) },
                                onClick = { activityLauncher.launch(AppUtils.getAppDetailSettingsIntent()) }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PrivacyAction(modifier: Modifier = Modifier, hasPermission: Boolean) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (hasPermission) "已授权" else "去设置")
            Icon(Icons.AutoMirrored.Rounded.NavigateNext, null)
        }
    }
}