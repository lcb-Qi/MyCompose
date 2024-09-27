package com.lcb.one.ui.screen.main.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.util.platform.AppUtils
import com.lcb.weight.settings.SettingSingleChoice
import com.lcb.weight.settings.SettingsCategory
import com.lcb.weight.settings.SettingsDefaults
import com.lcb.weight.settings.SettingsMenuLink
import com.lcb.weight.settings.SettingsSwitch

@Composable
fun SettingsPage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val colors = SettingsDefaults.colorsOnCard()

        // 1.主题
        SettingsCategory(title = stringResource(R.string.interface_and_display)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                // amoled
                SettingsSwitch(
                    colors = colors,
                    icon = { Icon(Icons.Rounded.PhoneAndroid, null) },
                    modifier = Modifier.padding(top = 8.dp),
                    title = "AMOLED",
                    summary = stringResource(R.string.amoled_mode_summary),
                    checked = ThemeManager.amoled,
                    onCheckedChange = { ThemeManager.amoled = it }
                )

                // 动态取色
                SettingsSwitch(
                    colors = colors,
                    icon = { Icon(Icons.Rounded.Palette, null) },
                    modifier = Modifier.padding(bottom = 8.dp),
                    title = stringResource(R.string.dynamic_color),
                    summary = stringResource(R.string.dynamic_color_summary),
                    checked = ThemeManager.dynamicColor,
                    onCheckedChange = { ThemeManager.dynamicColor = it }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 2.深色模式
        SettingsCategory(title = stringResource(R.string.dark_mode)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                val options = stringArrayResource(R.array.settings_ui_mode_options)
                val values = ThemeManager.uiModes
                SettingSingleChoice(
                    icon = { Icon(Icons.Rounded.DarkMode, null) },
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors = colors,
                    title = stringResource(R.string.dark_mode),
                    options = options,
                    selectIndex = values.indexOf(ThemeManager.uiMode),
                    onItemSelected = { ThemeManager.uiMode = values[it] }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 3.其他设置
        SettingsCategory(title = stringResource(R.string.other)) {
            Card {
                // 权限
                SettingsMenuLink(
                    colors = colors,
                    modifier = Modifier.padding(top = 8.dp),
                    title = stringResource(R.string.permissions_management),
                    icon = { Icon(Icons.Rounded.PrivacyTip, null) },
                    onClick = { navController.launchSingleTop(PrivacyScreen) }
                )

                // 版本信息
                SettingsMenuLink(
                    colors = SettingsDefaults.colorsOnCard(),
                    title = stringResource(R.string.version_info),
                    summary = BuildConfig.VERSION_NAME,
                    icon = { Icon(Icons.Rounded.Info, null) },
                    onClick = { }
                )

                // 项目地址
                val url = "https://github.com/lcb-Qi/MyCompose"
                SettingsMenuLink(
                    colors = SettingsDefaults.colorsOnCard(),
                    modifier = Modifier.padding(bottom = 8.dp),
                    title = stringResource(R.string.project_url),
                    summary = url,
                    icon = { Icon(Icons.Rounded.Link, null) },
                    onClick = { AppUtils.launchSystemBrowser(uri = url.toUri()) }
                )
            }
        }
    }
}
