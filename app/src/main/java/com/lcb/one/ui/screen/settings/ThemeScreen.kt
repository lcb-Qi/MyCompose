package com.lcb.one.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.screen.tester.widget.toHex
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.SettingsSwitch

@Composable
fun ThemeScreen() {
    Scaffold(topBar = { ToolBar(title = "主题") }) { paddingValues ->
        Column(
            Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
        ) {
            SettingsSwitch(
                title = stringResource(R.string.settings_dynamic_color_title),
                summary = stringResource(R.string.settings_dynamic_color_summary),
                checked = ThemeManager.dynamicColor
            ) {
                ThemeManager.changeDynamicColor(it)
            }

            AnimatedVisibility(visible = !ThemeManager.dynamicColor) {
                LazyColumn {
                    val seeds = ThemeManager.seeds
                    items(count = seeds.size, key = { seeds[it].toArgb() }) {

                        val seed = seeds[it]
                        val onclick: () -> Unit = { ThemeManager.changeTheme(seed) }

                        ListItem(
                            modifier = Modifier.clickable(role = Role.RadioButton) { onclick() },
                            headlineContent = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        painter = ColorPainter(seed),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(48.dp, 48.dp)
                                            .clip(MaterialTheme.shapes.small)
                                    )

                                    Text(text = seed.toHex(), modifier = Modifier.weight(1f))
                                }
                            },
                            trailingContent = {
                                RadioButton(
                                    selected = ThemeManager.currentTheme == seed,
                                    onClick = onclick
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
