package com.lcb.one.ui.screen.settings

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.settings.widget.ThemSelector
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingSingleChoice
import com.lcb.one.ui.widget.settings.ui.SettingsCategory
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSwitch
import com.lcb.one.util.android.Res

object ThemeSettingsScreen : Screen {
    override val route: String
        get() = "ThemeSettings"

    override val label: String
        get() = Res.string(R.string.theme)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        Scaffold(topBar = { ToolBar(title = label) }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                SettingsCategory(title = stringResource(R.string.theme)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                            SimpleSettingsSwitch(
                                modifier = Modifier.padding(top = 8.dp),
                                title = "AMOLED",
                                summary = stringResource(R.string.amoled_mode_summary),
                                checked = ThemeManager.amoledMode,
                                onCheckedChange = { ThemeManager.amoledMode = it }
                            )

                            val bottomPadding by animateDpAsState(
                                targetValue = if (ThemeManager.dynamicColor) 8.dp else 0.dp,
                                label = "dynamicColor"
                            )
                            SimpleSettingsSwitch(
                                modifier = Modifier.padding(bottom = bottomPadding),
                                title = stringResource(R.string.dynamic_color),
                                summary = stringResource(R.string.dynamic_color_summary),
                                checked = ThemeManager.dynamicColor,
                                onCheckedChange = { ThemeManager.dynamicColor = it }
                            )

                            AnimatedVisibility(visible = !ThemeManager.dynamicColor) {
                                ThemSelector(
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    selected = ThemeManager.themeColor,
                                    onColorSelected = { ThemeManager.themeColor = it }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                SettingsCategory(title = stringResource(R.string.dark_mode)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        val options = stringArrayResource(R.array.settings_dark_mode_options)
                        val values = ThemeManager.darkModeValues
                        SettingSingleChoice(
                            modifier = Modifier.padding(vertical = 8.dp),
                            colors = SettingsDefaults.colorOnCard(),
                            title = stringResource(R.string.dark_mode),
                            options = options,
                            selectIndex = values.indexOf(ThemeManager.darkMode),
                            onItemSelected = {
                                ThemeManager.darkMode = values[it]
                            }
                        )
                    }
                }
            }
        }
    }
}
