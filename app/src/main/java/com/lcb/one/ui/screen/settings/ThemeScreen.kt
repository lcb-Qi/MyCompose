package com.lcb.one.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
                title = "AMOLED",
                summary = "启用纯白/纯黑色背景",
                checked = ThemeManager.amoledMode,
                onCheckedChange = { ThemeManager.amoledMode = it }
            )

            SettingsSwitch(
                title = stringResource(R.string.settings_dynamic_color_title),
                summary = stringResource(R.string.settings_dynamic_color_summary),
                checked = ThemeManager.dynamicColor,
                onCheckedChange = { ThemeManager.dynamicColor = it }
            )

            AnimatedVisibility(visible = !ThemeManager.dynamicColor) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val seeds = ThemeManager.seeds
                    items(count = seeds.size, key = { seeds[it].toArgb() }) { index ->
                        val seed = seeds[index]
                        val checked = seed == ThemeManager.themeColor
                        Surface(
                            color = seed,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.requiredSize(48.dp),
                            checked = checked,
                            onCheckedChange = { if (it) ThemeManager.themeColor = seed }
                        ) {
                            if (checked) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
