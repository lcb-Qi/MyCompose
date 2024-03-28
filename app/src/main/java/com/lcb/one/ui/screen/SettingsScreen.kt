package com.lcb.one.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.lcb.one.ui.widget.settings.ui.SettingsListDropdown
import com.lcb.one.ui.widget.settings.ui.SettingsSwitch
import com.lcb.one.R
import com.lcb.one.ui.AppSettings
import com.lcb.one.ui.widget.AppBar

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = { AppBar(title = stringResource(R.string.settings)) }) { paddingValues ->
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
                title = { SettingsTitle(stringResource(R.string.settings_dynamic_color_title)) },
                summary = { SettingsSummary(stringResource(R.string.settings_dynamic_color_summary)) },
                checked = AppSettings.appDynamicColor
            ) {
                AppSettings.appDynamicColor = it
            }

            // 标题更新间隔
            val options = stringArrayResource(R.array.settings_duration_options)
            val values = integerArrayResource(R.array.settings_duration_values)
            SettingsListDropdown(
                title = { SettingsTitle(stringResource(R.string.settings_poem_update_duration_title)) },
                selectIndex = values.indexOf(AppSettings.poemUpdateDuration).coerceAtLeast(0),
                items = options.toList(),
                onItemSelected = { index, _ ->
                    AppSettings.poemUpdateDuration = values[index]
                }
            )
        }
    }
}

@Composable
fun SettingsTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun SettingsSummary(summary: String) {
    Text(text = summary, style = MaterialTheme.typography.bodySmall)
}