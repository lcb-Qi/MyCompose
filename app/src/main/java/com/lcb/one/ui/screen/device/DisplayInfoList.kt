package com.lcb.one.ui.screen.device

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.lcb.one.bean.DisplayInfo

@Composable
fun DisplayInfoList() {
    val displayInfo by remember { mutableStateOf(DisplayInfo.obtain()) }
    val map = mapOf(
        "Resolution" to displayInfo.resolution.toString() + " px",
        "Display Size" to displayInfo.displaySize.toString() + " px",
        "Small Width" to displayInfo.smallWidth.toString() + " dp",
        "DPI" to displayInfo.dpi.toString(),
        "Density" to displayInfo.density.toString(),
        "StatusBars Height" to displayInfo.statusBarsHeight.toString() + " px",
        "NavigationBars Height" to displayInfo.navigationBarsHeight.toString() + " px",
    )


    LazyColumn {
        map.forEach { (key, value) ->
            item {
                ListItem(
                    headlineContent = {
                        Text(text = key, style = MaterialTheme.typography.titleMedium)
                    },
                    supportingContent = {
                        Text(text = value, style = MaterialTheme.typography.bodyMedium)
                    }
                )
            }
        }
    }
}