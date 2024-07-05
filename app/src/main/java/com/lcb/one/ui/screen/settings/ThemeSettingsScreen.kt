package com.lcb.one.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.SettingsNavGraph
import com.lcb.one.ui.screen.settings.widget.ThemSelector
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSwitch
import com.ramcosta.composedestinations.annotation.Destination

@Destination<SettingsNavGraph>
@Composable
fun ThemeSettingsScreen() {
    Scaffold(topBar = { ToolBar(title = Localization.theme) }) { padding ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                SimpleSettingsSwitch(
                    title = "AMOLED",
                    summary = Localization.amoledModeSummary,
                    checked = ThemeManager.amoledMode,
                    onCheckedChange = { ThemeManager.amoledMode = it }
                )

                SimpleSettingsSwitch(
                    title = Localization.dynamicColor,
                    summary = Localization.dynamicColorSummary,
                    checked = ThemeManager.dynamicColor,
                    onCheckedChange = { ThemeManager.dynamicColor = it }
                )

                AnimatedVisibility(visible = !ThemeManager.dynamicColor) {
                    ThemSelector(
                        selected = ThemeManager.themeColor,
                        onColorSelected = { ThemeManager.themeColor = it }
                    )
                }
            }
        }
    }
}
