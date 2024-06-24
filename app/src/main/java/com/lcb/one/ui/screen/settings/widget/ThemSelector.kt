package com.lcb.one.ui.screen.settings.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.theme.ThemeManager

@Composable
fun ThemSelector(
    modifier: Modifier = Modifier,
    selected: Color,
    onColorSelected: (Color) -> Unit = {}
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val seeds = ThemeManager.seeds
        items(count = seeds.size, key = { seeds[it].toArgb() }) { index ->
            val seed = seeds[index]
            val checked = seed == selected
            Surface(
                color = seed,
                shape = RoundedCornerShape(50),
                modifier = Modifier.requiredSize(48.dp),
                checked = checked,
                onCheckedChange = { if (it) onColorSelected(seed) }
            ) {
                if (checked) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}