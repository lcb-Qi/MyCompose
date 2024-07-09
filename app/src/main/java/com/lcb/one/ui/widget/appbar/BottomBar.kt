package com.lcb.one.ui.widget.appbar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class BottomBarItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomBar(selectedIndex: Int, items: List<BottomBarItem>, onItemChanged: (Int) -> Unit) {
    require(selectedIndex in items.indices) {
        "selectedIndex must in items.indices(${items.indices})"
    }
    NavigationBar {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            NavigationBarItem(
                selected = selected,
                onClick = { onItemChanged(index) },
                label = { Text(item.label, style = MaterialTheme.typography.labelMedium) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) })
        }
    }
}