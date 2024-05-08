package com.lcb.one.ui.screen.main

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.lcb.one.R
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.util.android.navigateSingleTop

@Composable
fun MoreScreen() {
    val navController = LocalNav.current!!
    Column {
        // 设置
        SettingsMenuLink(
            title = stringResource(R.string.settings),
            icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.SETTINGS)
        }

        // 关于
        SettingsMenuLink(
            title = "关于${stringResource(R.string.app_name)}",
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.ABOUT)
        }
    }
}
