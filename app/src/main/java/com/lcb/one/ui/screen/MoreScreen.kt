package com.lcb.one.ui.screen

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
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.FriendlyExitHandler
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SettingsSimpleText
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.PACKAGE_ME

fun NavController.navigateSingleTop(route: String) {
    navigate(route = route, navOptions = navOptions { launchSingleTop = true })
}

@Composable
fun MoreScreen(navController: NavController) {
    FriendlyExitHandler()

    Column {
        // 设置
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.settings)) },
            icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(Route.SETTINGS)
        }

        // 项目地址
        val url = stringResource(R.string.project_location_url)
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.project_location)) },
            summary = { SettingsSummary(url) },
            icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = "") }
        ) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            MyApp.getAppContext().startActivity(intent)
        }

        // 版本信息
        val versionName = AppUtils.getAppVersionName(packageName = PACKAGE_ME)
        val buildTime = stringResource(R.string.BUILD_TIME)
        val versionInfo = "$versionName($buildTime)"
        SettingsSimpleText(
            title = { SettingsTitle(stringResource(R.string.version_info)) },
            summary = { SettingsSummary(versionInfo) },
            icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = "") }
        ) {
        }
    }
}