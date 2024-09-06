package com.lcb.one.ui.screen.main.tool

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.applist.InstalledAppsScreen
import com.lcb.one.ui.screen.device.DeviceInfoScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeviceTool(navController: NavHostController) {
    ToolCard(
        title = stringResource(R.string.device),
        icon = { Icon(Icons.Rounded.PhoneAndroid, "") }
    ) {
        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(DeviceInfoScreen) },
            label = { Text(text = DeviceInfoScreen.label) }
        )

        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(InstalledAppsScreen) },
            label = { Text(text = InstalledAppsScreen.label) }
        )
    }
}