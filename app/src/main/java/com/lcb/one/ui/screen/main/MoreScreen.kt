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

        // 项目地址
        val url = stringResource(R.string.project_location_url)
        SettingsMenuLink(
            title = stringResource(R.string.project_location),
            summary = url,
            icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = "") }
        ) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            MyApp.getAppContext().startActivity(intent)
        }

        // 关于
        SettingsMenuLink(
            title = "关于",
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.ABOUT)
        }
    }
}
