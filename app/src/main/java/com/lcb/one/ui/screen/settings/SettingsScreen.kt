package com.lcb.one.ui.screen.settings

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.SettingSingleChoice
import com.lcb.one.ui.widget.settings.ui.SettingsCategory
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSwitch
import com.lcb.one.util.platform.Res

object SettingsScreen : Screen() {
    override val label: String = Res.string(R.string.settings)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        Scaffold(topBar = { ToolBar(title = label) }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // 界面显示
                SettingsCategory(title = stringResource(R.string.interface_and_display)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        val colors = SettingsDefaults.colorsOnCard()
                        SimpleSettingsSwitch(
                            colors = colors,
                            icon = { Icon(Icons.Rounded.PhoneAndroid, null) },
                            modifier = Modifier.padding(top = 8.dp),
                            title = "AMOLED",
                            summary = stringResource(R.string.amoled_mode_summary),
                            checked = ThemeManager.amoled,
                            onCheckedChange = { ThemeManager.amoled = it }
                        )

                        SimpleSettingsSwitch(
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

                // 深色模式
                SettingsCategory(title = stringResource(R.string.dark_mode)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        val options = stringArrayResource(R.array.settings_ui_mode_options)
                        val values = ThemeManager.uiModes
                        SettingSingleChoice(
                            icon = { Icon(Icons.Rounded.DarkMode, null) },
                            modifier = Modifier.padding(vertical = 8.dp),
                            colors = SettingsDefaults.colorsOnCard(),
                            title = stringResource(R.string.dark_mode),
                            options = options,
                            selectIndex = values.indexOf(ThemeManager.uiMode),
                            onItemSelected = { ThemeManager.uiMode = values[it] }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2.其他设置
                SettingsCategory(title = stringResource(R.string.other)) {
                    Card {
                        val colors = SettingsDefaults.colorsOnCard()
                        // 权限
                        SimpleSettingsMenuLink(
                            colors = colors,
                            modifier = Modifier.padding(vertical = 8.dp),
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
