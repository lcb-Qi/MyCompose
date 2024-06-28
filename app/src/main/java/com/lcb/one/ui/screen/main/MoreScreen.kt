package com.lcb.one.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.route.NavGraphs
import com.lcb.one.route.destinations.AboutScreenDestination
import com.lcb.one.ui.navController
import com.lcb.one.ui.widget.common.listItemColorOnCard
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink

@Composable
fun MoreScreen() {
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column {
            // 设置
            SimpleSettingsMenuLink(
                colors = listItemColorOnCard(),
                title = Localization.settings,
                icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = null) },
                onClick = { navController.navigate(NavGraphs.settings) }
            )

            // 关于
            SimpleSettingsMenuLink(
                colors = listItemColorOnCard(),
                title = "${Localization.about}${Localization.appName}",
                icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = null) },
                onClick = { navController.navigate(AboutScreenDestination) }
            )
        }
    }
}
