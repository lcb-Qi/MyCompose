package com.lcb.one.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun AppBottomBars(defaultIndex: Int, items: List<BottomBarItem>, onClick: (Int) -> Unit) {
    require(defaultIndex in items.indices) {
        throw IllegalArgumentException("selectedIndex must in items.indices(${items.indices})")
    }
    NavigationBar {
        var currentIndex by remember { mutableIntStateOf(defaultIndex) }
        items.forEachIndexed { index, item ->
            val selected = currentIndex == index
            NavigationBarItem(
                selected = selected,
                onClick = {
                    currentIndex = index
                    onClick(currentIndex)
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelMedium) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) })
        }
    }
}