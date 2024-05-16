package com.lcb.one.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.settings.ui.SettingsSwitch

@Composable
fun ThemeScreen() {
    Scaffold(topBar = { ToolBar(title = Localization.theme) }) { paddingValues ->
        Column(
            Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
        ) {
            SettingsSwitch(
                title = "AMOLED",
                summary = Localization.amoledModeSummary,
                checked = ThemeManager.amoledMode,
                onCheckedChange = { ThemeManager.amoledMode = it }
            )

            SettingsSwitch(
                title = Localization.dynamicColor,
                summary = Localization.dynamicColorSummary,
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
