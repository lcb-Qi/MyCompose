package com.lcb.one.ui.screen.settings

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Screen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingSingleChoice
import com.lcb.one.ui.widget.settings.ui.SettingsCategory
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSwitch

object SettingsScreen : Screen {
    override val route: String
        get() = "Settings"

    @Composable
    override fun Content(args: Bundle?) {
        val navController = LocalNav.current!!
        Scaffold(topBar = { ToolBar(title = Localization.settings) }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // 1.通用设置
                SettingsCategory(title = Localization.common) {
                    Card {
                        ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                            // 内置浏览器
                            SimpleSettingsSwitch(
                                modifier = Modifier.padding(top = 8.dp),
                                checked = AppGlobalConfigs.useBuiltinBrowser,
                                icon = { Icon(Icons.Rounded.TravelExplore, null) },
                                title = "使用内置浏览器",
                                onCheckedChange = { AppGlobalConfigs.useBuiltinBrowser = it }
                            )

                            // 主题
                            SimpleSettingsMenuLink(
                                title = Localization.theme,
                                icon = { Icon(Icons.Rounded.Palette, null) },
                                onClick = { navController.launchSingleTop(ThemeSettingsScreen) }
                            )

                            // 语言
                            SimpleSettingsMenuLink(
                                modifier = Modifier.padding(bottom = 8.dp),
                                enabled = false,
                                title = "语言",
                                icon = { Icon(Icons.Rounded.Language, null) },
                                onClick = { }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2.其他设置
                SettingsCategory(title = Localization.other) {
                    Card {
                        ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                            // 标题诗词更新间隔
                            val options = stringArrayResource(R.array.settings_duration_options)
                            val values = integerArrayResource(R.array.settings_duration_values)
                            SettingSingleChoice(
                                modifier = Modifier.padding(top = 8.dp),
                                title = Localization.poemUpdateDuration,
                                icon = { Icon(Icons.Rounded.Autorenew, null) },
                                selectIndex = values.indexOf(AppGlobalConfigs.poemUpdateInterval)
                                    .coerceAtLeast(0),
                                options = options,
                                onItemSelected = {
                                    AppGlobalConfigs.poemUpdateInterval = values[it]
                                }
                            )

                            // 权限
                            SimpleSettingsMenuLink(
                                modifier = Modifier.padding(bottom = 8.dp),
                                title = "权限管理",
                                icon = { Icon(Icons.Rounded.PrivacyTip, null) },
                                onClick = { navController.launchSingleTop(PrivacyScreen) }
                            )
                        }
                    }
                }
            }
        }
    }
}
