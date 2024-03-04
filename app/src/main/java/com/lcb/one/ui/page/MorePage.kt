package com.lcb.one.ui.page

import android.content.Intent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink
import com.lcb.one.ui.widget.settings.ui.SettingsSimpleText
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.PACKAGE_ME

fun NavController.navigateSingleTop(route: String) {
    navigate(route = route, navOptions = navOptions { launchSingleTop = true })
}

@Composable
fun MorePage(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = RouteConfig.MORE,
        enterTransition = { slideInHorizontally(animationSpec = tween(500)) { it } },
        exitTransition = { slideOutHorizontally(animationSpec = tween(500)) { -it } },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(500)) { -it } },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(500)) { it } },
        modifier = modifier
    ) {
        composable(RouteConfig.MORE) { MorePageImpl(navController) }
        composable(RouteConfig.SETTINGS) { SettingsPage() }
    }
}

@Composable
private fun MorePageImpl(navController: NavController) {
    Column {
        // 设置
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.setting)) },
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(RouteConfig.SETTINGS)
        }

        // 项目地址
        val url = stringResource(R.string.project_location_url)
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.project_location)) },
            summary = { SettingsSummary(url) },
            icon = { Icon(imageVector = Icons.Filled.Link, contentDescription = "") }
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
            icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "") }
        ) {
        }
    }
}