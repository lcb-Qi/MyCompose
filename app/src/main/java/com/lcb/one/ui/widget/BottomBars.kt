package com.lcb.one.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.lcb.one.ui.theme.labelMedium
import java.lang.IllegalArgumentException

data class BottomBarItem(
    val id: Int,
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomBars(selectedIndex: Int, items: List<BottomBarItem>, onClick: (Int) -> Unit) {
    require(selectedIndex in items.indices) {
        throw IllegalArgumentException("selectedIndex must in items.indices(${items.indices})")
    }
    NavigationBar {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            NavigationBarItem(
                selected = selected,
                onClick = { onClick(item.id) },
                label = { Text(item.label, style = labelMedium()) },
                icon = { Icon(imageVector = item.icon, contentDescription = "") })
        }
    }
}