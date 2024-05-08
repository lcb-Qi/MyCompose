package com.lcb.one.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.settings.ui.SettingsListDropdown
import com.lcb.one.ui.widget.settings.ui.SettingsSwitch
import com.lcb.one.R
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
    Scaffold(topBar = { ToolBar(title = stringResource(R.string.settings)) }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            // 通用
            SettingsGroup(title = "通用") {
                SettingsMenuLink(
                    title = "主题",
                    icon = { Icon(Icons.Rounded.Palette, "") }
                ) {
                    navHostController.navigateSingleTop(Route.THEME)
                }
            }

            SettingsGroup(title = "其他") {
                // 标题更新间隔
                val options = stringArrayResource(R.array.settings_duration_options)
                val values = integerArrayResource(R.array.settings_duration_values)
                SettingsListDropdown(
                    title = stringResource(R.string.settings_poem_update_duration_title),
                    icon = { Icon(imageVector = Icons.Rounded.Autorenew, contentDescription = "") },
                    selectIndex = values.indexOf(AppGlobalConfigs.poemUpdateDuration)
                        .coerceAtLeast(0),
                    items = options.toList(),
                    onItemSelected = { index, _ ->
                        AppGlobalConfigs.poemUpdateDuration = values[index]
                    }
                )
            }
        }
    }
}