package com.lcb.one.ui.screen.device.widget

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.lcb.one.ui.screen.device.repo.model.BasicInfo

@Composable
fun BasicInfoList() {
    val deviceInfo by remember { mutableStateOf(BasicInfo.obtain()) }
    val map = mapOf(
        "Device Model" to deviceInfo.deviceModel,
        "Brand" to deviceInfo.brand,
        "OS Version" to deviceInfo.osVersion,
        "SDK Version" to deviceInfo.sdkVersion.toString()
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