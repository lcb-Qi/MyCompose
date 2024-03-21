package com.lcb.one.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.R
import com.lcb.one.ui.AppSettings
import com.lcb.one.ui.Route
import com.lcb.one.ui.RouteScreen
import com.lcb.one.ui.glance.PoemAppWidget
import com.lcb.one.ui.screen.BiliBiliScreen
import com.lcb.one.ui.screen.DeviceInfoScreen
import com.lcb.one.ui.screen.HomeScreen
import com.lcb.one.ui.screen.MoreScreen
import com.lcb.one.ui.screen.SettingsScreen
import com.lcb.one.viewmodel.PoemViewModel
import com.lcb.one.ui.screen.ToolScreen
import com.lcb.one.ui.screen.navigateSingleTop
import com.lcb.one.ui.widget.AppNavHost
import com.lcb.one.ui.widget.TopAppBars
import com.lcb.one.ui.widget.AppThemeSurface
import com.lcb.one.ui.widget.AppBottomBars
import com.lcb.one.ui.widget.BottomBarItem
import com.lcb.one.ui.widget.dialog.LoadingDialog
import com.lcb.one.ui.widget.dialog.PoemInfoDialog
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val poemViewModel by viewModels<PoemViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppScreen() }
        poemViewModel.refresh(false)
        lifecycleScope.launch {
            PoemAppWidget().updateAll(this@MainActivity)
        }
    }

    @Composable
    private fun AppScreen() {
        AppThemeSurface {
            var onTitleClick: (() -> Unit)? = null
            var onTitleLongClick: (() -> Unit)? = null
            val navController = rememberNavController()
            val routes = createRouter(navController)
            val bottomItem = createBottomBarItem()

            Scaffold(
                topBar = {
                    TopAppBars(
                        text = AppSettings.appTitle,
                        onClick = onTitleClick,
                        onLongClick = onTitleLongClick,
                        navigationIcon = {
                            val route = navController.currentDestination?.route
                            if (route != Route.HOME && route != Route.TOOL && route != Route.MORE) {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            }
                        }
                    )
                },
                bottomBar = {
                    AppBottomBars(0, bottomItem) {
                        when (it) {
                            0 -> navController.navigateSingleTop(Route.HOME)
                            1 -> navController.navigateSingleTop(Route.TOOL)
                            2 -> navController.navigateSingleTop(Route.MORE)
                        }
                    }
                }
            ) { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding()
                val bottomPadding = paddingValues.calculateBottomPadding()

                val poemInfo by poemViewModel.poemFlow.collectAsState()
                val isLoading by poemViewModel.isLoading.collectAsState()
                var showDetail by remember { mutableStateOf(false) }

                AppSettings.appTitle = poemInfo.recommend
                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStack ->
                        val route = backStack.destination.route
                        AppSettings.appTitle = if (route == Route.HOME) {
                            onTitleClick = { poemViewModel.refresh(true) }
                            onTitleLongClick = { showDetail = true }
                            poemInfo.recommend
                        } else {
                            onTitleClick = null
                            onTitleLongClick = null
                            routes.find { it.route == route }?.title ?: getString(R.string.app_name)
                        }
                    }
                }

                AppNavHost(
                    navController = navController,
                    startDestination = Route.HOME,
                    modifier = Modifier
                        .padding(top = topPadding, bottom = bottomPadding)
                        .fillMaxSize()
                ) {
                    routes.forEach { routeItem ->
                        composable(routeItem.route) { routeItem.content() }
                    }
                }

                PoemInfoDialog(
                    showDetail,
                    poemDetail = poemInfo.origin,
                    onDismissRequest = { showDetail = false },
                    onConfirm = { showDetail = false }
                )
                LoadingDialog(isLoading)
            }
        }
    }

    private fun createBottomBarItem(): List<BottomBarItem> {
        return listOf(
            BottomBarItem(getString(R.string.home), Icons.Rounded.Home),
            BottomBarItem(getString(R.string.tool), Icons.Rounded.Android),
            BottomBarItem(getString(R.string.more), Icons.Rounded.MoreHoriz)
        )
    }

    private fun createRouter(navController: NavHostController): List<RouteScreen> {
        return listOf(
            RouteScreen(Route.HOME, getString(R.string.home)) { HomeScreen() },
            RouteScreen(Route.TOOL, getString(R.string.tool)) { ToolScreen(navController) },
            RouteScreen(Route.MORE, getString(R.string.more)) { MoreScreen(navController) },
            RouteScreen(Route.BILI, getString(R.string.bibibili)) { BiliBiliScreen() },
            RouteScreen(Route.DEVICE, getString(R.string.device_info)) { DeviceInfoScreen() },
            RouteScreen(Route.SETTINGS, getString(R.string.settings)) { SettingsScreen() },
        )
    }
}