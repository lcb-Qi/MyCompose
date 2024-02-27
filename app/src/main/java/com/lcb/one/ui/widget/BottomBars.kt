package com.lcb.one.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.lcb.one.ui.theme.labelMedium

data class BottomBarItem(val id: Int, val name: String, val icon: ImageVector)

@Composable
fun BottomBars(selectedIndex: Int, items: List<BottomBarItem>, onClick: (Int) -> Unit) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            NavigationBarItem(
                selected = selected,
                onClick = { onClick(item.id) },
                label = { Text(item.name, style = labelMedium()) },
                icon = { Icon(imageVector = item.icon, contentDescription = "") })
        }
    }
}