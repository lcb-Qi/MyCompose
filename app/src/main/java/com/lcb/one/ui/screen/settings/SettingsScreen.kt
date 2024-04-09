package com.lcb.one.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.lcb.one.ui.widget.settings.ui.SettingsListDropdown
import com.lcb.one.ui.widget.settings.ui.SettingsSwitch
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.LLog

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = { ToolBar(title = stringResource(R.string.settings)) }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            // 动态取色
            SettingsSwitch(
                title = stringResource(R.string.settings_dynamic_color_title),
                summary = stringResource(R.string.settings_dynamic_color_summary),
                checked = AppGlobalConfigs.appDynamicColor
            ) {
                AppGlobalConfigs.appDynamicColor = it
            }

            // 标题更新间隔
            val options = stringArrayResource(R.array.settings_duration_options)
            val values = integerArrayResource(R.array.settings_duration_values)
            SettingsListDropdown(
                title = stringResource(R.string.settings_poem_update_duration_title),
                selectIndex = values.indexOf(AppGlobalConfigs.poemUpdateDuration).coerceAtLeast(0),
                items = options.toList(),
                onItemSelected = { index, _ ->
                    AppGlobalConfigs.poemUpdateDuration = values[index]
                }
            )
        }
    }
}