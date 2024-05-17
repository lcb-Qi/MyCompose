package com.lcb.one.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import com.lcb.one.ui.widget.settings.ui.SettingsListDropdown
import com.lcb.one.R
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.SettingsGroup
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.util.android.navigateSingleTop

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val navHostController = LocalNav.current!!
    Scaffold(topBar = { ToolBar(title = Localization.settings) }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            // 通用
            SettingsGroup(title = Localization.common) {
                SettingsMenuLink(
                    title = Localization.theme,
                    icon = { Icon(Icons.Rounded.Palette, "") }
                ) {
                    navHostController.navigateSingleTop(Route.THEME)
                }
            }

            SettingsGroup(title = Localization.other) {
                // 标题更新间隔
                val options = stringArrayResource(R.array.settings_duration_options)
                val values = integerArrayResource(R.array.settings_duration_values)
                SettingsListDropdown(
                    title = Localization.poemUpdateDuration,
                    icon = { Icon(imageVector = Icons.Rounded.Autorenew, contentDescription = "") },
                    selectIndex = values.indexOf(AppGlobalConfigs.poemUpdateInterval).coerceAtLeast(0),
                    items = options.toList(),
                    onItemSelected = { index, _ ->
                        AppGlobalConfigs.poemUpdateInterval = values[index]
                    }
                )
            }
        }
    }
}