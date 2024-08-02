package com.lcb.one.ui.screen.settings

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.DisplaySettings
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
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
import com.lcb.one.util.android.Res

object SettingsScreen : Screen {
    override val route: String
        get() = "Settings"

    override val label: String
        get() = Res.string(R.string.settings)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        Scaffold(topBar = { ToolBar(title = label) }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // 1.通用设置
                SettingsCategory(title = stringResource(R.string.common)) {
                    Card {
                        ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                            // 内置浏览器
                            SimpleSettingsSwitch(
                                modifier = Modifier.padding(top = 8.dp),
                                checked = AppGlobalConfigs.useBuiltinBrowser,
                                icon = { Icon(Icons.Rounded.TravelExplore, null) },
                                title = stringResource(R.string.use_built_in_browser),
                                onCheckedChange = { AppGlobalConfigs.useBuiltinBrowser = it }
                            )

                            // 界面显示
                            SimpleSettingsMenuLink(
                                modifier = Modifier.padding(bottom = 8.dp),
                                title = stringResource(R.string.interface_and_display),
                                icon = { Icon(Icons.Rounded.DisplaySettings, null) },
                                onClick = { navController.launchSingleTop(ThemeSettingsScreen) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2.其他设置
                SettingsCategory(title = stringResource(R.string.other)) {
                    Card {
                        ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                            // 标题诗词更新间隔
                            val options = stringArrayResource(R.array.settings_duration_options)
                            val values = integerArrayResource(R.array.settings_duration_values)
                            SettingSingleChoice(
                                modifier = Modifier.padding(top = 8.dp),
                                title = stringResource(R.string.poem_update_duration),
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
                                title = stringResource(R.string.permissions_management),
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
