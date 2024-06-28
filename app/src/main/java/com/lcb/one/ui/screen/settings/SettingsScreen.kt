package com.lcb.one.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
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
import com.lcb.one.route.destinations.ThemeSettingsScreenDestination
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.SettingsNavGraph
import com.lcb.one.ui.navController
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.listItemColorOnCard
import com.lcb.one.ui.widget.settings.ui.SettingSingleChoice
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsGroup
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink
import com.ramcosta.composedestinations.annotation.Destination

@Destination<SettingsNavGraph>(start = true)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = { ToolBar(title = Localization.settings) }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            // 通用
            SimpleSettingsGroup(title = Localization.common) {
                Card {
                    // 主题
                    SimpleSettingsMenuLink(
                        colors = listItemColorOnCard(),
                        title = Localization.theme,
                        icon = { Icon(Icons.Rounded.Palette, null) },
                        onClick = { navController.navigate(ThemeSettingsScreenDestination) }
                    )

                    // 语言
                    SimpleSettingsMenuLink(
                        enabled = false,
                        colors = listItemColorOnCard(),
                        title = "语言",
                        icon = { Icon(Icons.Rounded.Language, null) },
                        onClick = { }
                    )
                }
            }

            SimpleSettingsGroup(title = Localization.other) {
                // 标题更新间隔
                val options = stringArrayResource(R.array.settings_duration_options)
                val values = integerArrayResource(R.array.settings_duration_values)
                Card {
                    SettingSingleChoice(
                        colors = listItemColorOnCard(),
                        title = Localization.poemUpdateDuration,
                        icon = { Icon(Icons.Rounded.Autorenew, null) },
                        selectIndex = values.indexOf(AppGlobalConfigs.poemUpdateInterval)
                            .coerceAtLeast(0),
                        options = options,
                        onItemSelected = { AppGlobalConfigs.poemUpdateInterval = values[it] }
                    )
                }
            }
        }
    }
}