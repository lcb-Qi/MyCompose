package com.lcb.one.ui.widget

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

private const val DURATION = 500/* ms */

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        contentAlignment = contentAlignment,
        route = route,
        enterTransition = { slideInHorizontally(animationSpec = tween(DURATION)) { it } },
        exitTransition = { slideOutHorizontally(animationSpec = tween(DURATION)) { -it } },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(DURATION)) { -it } },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(DURATION)) { it } },
        builder = builder
    )
}