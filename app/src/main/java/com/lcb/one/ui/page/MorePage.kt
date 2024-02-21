package com.lcb.one.ui.page

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lcb.one.R
import com.lcb.one.ui.widget.settings.ui.SettingsMenuLink

fun NavController.navigateSingleTop(route: String) {
    navigate(route = route, navOptions = navOptions {
        launchSingleTop = true
    })
}

@Composable
fun MorePage(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = RouteConfig.MORE,
        enterTransition = { slideInVertically(animationSpec = tween(500)) { it } },
        exitTransition = { slideOutVertically(animationSpec = tween(500)) { -it } },
        modifier = modifier
    ) {
        composable(RouteConfig.MORE) { MorePageImpl(navController) }
        composable(RouteConfig.SETTINGS) { SettingsPage() }
    }
}

@Composable
private fun MorePageImpl(navController: NavController) {
    Column {
        SettingsMenuLink(
            title = { SettingsTitle(stringResource(R.string.setting)) },
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "") }
        ) {
            navController.navigateSingleTop(RouteConfig.SETTINGS)
        }
    }
}