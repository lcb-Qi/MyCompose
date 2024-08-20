package com.lcb.one.ui.screen.main.tool

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.screen.clock.ClockScreen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.bilibili.BiliBiliScreen
import com.lcb.one.ui.screen.menstruationAssistant.MenstruationAssistantScreen
import com.lcb.one.ui.screen.player.MusicPlayerScreen
import com.lcb.one.ui.screen.qmc.QmcConverterScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OtherTool(navController: NavHostController) {
    ToolCard(
        title = stringResource(R.string.other),
        icon = { Icon(Icons.Rounded.MiscellaneousServices, null) }
    ) {
        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(BiliBiliScreen) },
            label = { Text(text = BiliBiliScreen.label) }
        )
        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(ClockScreen) },
            label = { Text(text = ClockScreen.label) }
        )
        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(MenstruationAssistantScreen) },
            label = { Text(text = MenstruationAssistantScreen.label) }
        )
        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(QmcConverterScreen) },
            label = { Text(text = QmcConverterScreen.label) }
        )
        ElevatedAssistChip(
            onClick = { navController.launchSingleTop(MusicPlayerScreen) },
            label = { Text(text = MusicPlayerScreen.label) }
        )
    }
}