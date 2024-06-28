package com.lcb.one.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

const val ANIMATE_DURATION = 500

object DefaultAnimation : NavHostAnimatedDestinationStyle() {
    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = { slideInHorizontally(animationSpec = tween(ANIMATE_DURATION)) { it } }


    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = { slideOutHorizontally(animationSpec = tween(ANIMATE_DURATION)) { -it } }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = { slideInHorizontally(animationSpec = tween(ANIMATE_DURATION)) { -it } }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = { slideOutHorizontally(animationSpec = tween(ANIMATE_DURATION)) { it } }
}

@NavHostGraph(defaultTransitions = DefaultAnimation::class)
annotation class AppNavGraph

@NavGraph<AppNavGraph>
annotation class SettingsNavGraph

@NavGraph<AppNavGraph>
annotation class MenstruationAssistantNavGraph

/**
 * fixme: ugly implementation
 */
lateinit var navController: DestinationsNavigator