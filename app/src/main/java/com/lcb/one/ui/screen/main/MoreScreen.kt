package com.lcb.one.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.lcb.one.localization.Localization
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.util.android.navigateSingleTop

@Composable
fun MoreScreen() {
    val navController = LocalNav.current!!
    Column {
        // 设置
        SettingsMenuLink(
            title = Localization.settings,
            icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.SETTINGS)
        }

        // 关于
        SettingsMenuLink(
            title = "${Localization.about}${Localization.appName}",
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.ABOUT)
        }
    }
}
