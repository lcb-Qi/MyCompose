package com.lcb.one.ui.screen.settings

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.settings.widget.ThemSelector
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsSwitch

object ThemeSettingsScreen : Screen {
    override val route: String
        get() = "ThemeSettings"

    @Composable
    override fun Content(args: Bundle?) {
        Scaffold(topBar = { ToolBar(title = Localization.theme) }) { innerPadding ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
                    SimpleSettingsSwitch(
                        modifier = Modifier.padding(top = 8.dp),
                        title = "AMOLED",
                        summary = Localization.amoledModeSummary,
                        checked = ThemeManager.amoledMode,
                        onCheckedChange = { ThemeManager.amoledMode = it }
                    )

                    val bottomPadding by animateDpAsState(
                        targetValue = if (ThemeManager.dynamicColor) 8.dp else 0.dp,
                        label = "dynamicColor"
                    )
                    SimpleSettingsSwitch(
                        modifier = Modifier.padding(bottom = bottomPadding),
                        title = Localization.dynamicColor,
                        summary = Localization.dynamicColorSummary,
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
    }
}
