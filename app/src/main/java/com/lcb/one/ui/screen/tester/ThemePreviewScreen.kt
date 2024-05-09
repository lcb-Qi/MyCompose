package com.lcb.one.ui.screen.tester

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.screen.tester.widget.addColorSchemeList
import com.lcb.one.ui.screen.tester.widget.addComponentsList
import com.lcb.one.ui.theme.ThemeManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.Point
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePreviewScreen(modifier: Modifier = Modifier) {
    val paletteStyles = PaletteStyle.entries
    var styleState by remember { mutableStateOf(paletteStyles.first()) }

    val seeds = ThemeManager.seeds
    var seedState by remember { mutableStateOf(seeds.first()) }

    val colorScheme = dynamicColorScheme(
        seedColor = seedState,
        isDark = isSystemInDarkTheme(),
        style = styleState
    )

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(topBar = { ToolBar(title = "ThemePreview") }) { paddingValues ->
            LazyColumn(
                modifier = modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
                contentPadding = PaddingValues(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(text = "调色板风格: ${styleState.name}")
                }
                item {
                    LazyRow {
                        item {
                            SingleChoiceSegmentedButtonRow(space = (-8).dp) {
                                paletteStyles.forEach {
                                    SegmentedButton(
                                        selected = it == styleState,
                                        onClick = { styleState = it },
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(text = it.name)
                                    }
                                }
                            }
                        }
                    }
                }

                addDivider()

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "种子色: ")
                        Point(color = seedState, size = 24.dp)
                    }
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(count = seeds.size, key = { seeds[it].toArgb() }) { index ->
                            val color = seeds[index]
                            val checked = seedState == color
                            Surface(
                                color = color,
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.size(48.dp),
                                checked = checked,
                                onCheckedChange = { if (it) seedState = color }
                            ) {
                                AnimatedVisibility(visible = checked) {
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

                addDivider()
                addColorSchemeList(colorScheme)

                addDivider()
                addComponentsList()
            }
        }
    }
}

private fun LazyListScope.addDivider() {
    item {
        HorizontalDivider(Modifier.padding(vertical = 16.dp))
    }
}
