package com.lcb.one.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcb.one.R
import com.lcb.one.ui.glance.PoemAppWidget
import com.lcb.one.ui.page.HomePage
import com.lcb.one.ui.page.MorePage
import com.lcb.one.ui.page.RouteConfig
import com.lcb.one.viewmodel.PoemViewModel
import com.lcb.one.ui.page.ToolPage
import com.lcb.one.ui.page.navigateSingleTop
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

    private val items by lazy {
        listOf(
            BottomBarItem(0, RouteConfig.HOME, getString(R.string.home), Icons.Rounded.Home),
            BottomBarItem(1, RouteConfig.TOOL, getString(R.string.tool), Icons.Rounded.Android),
            BottomBarItem(2, RouteConfig.MORE, getString(R.string.more), Icons.Rounded.MoreHoriz)
        )
    }

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
            var selectedIndex by remember { mutableIntStateOf(0) }

            val poemInfo by poemViewModel.poemFlow.collectAsState()
            val isLoading by poemViewModel.isLoading.collectAsState()
            var showDetail by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    TopAppBars(
                        text = poemInfo.recommend,
                        onClick = { poemViewModel.refresh(true) },
                        onLongClick = { showDetail = true }
                    )
                },
                bottomBar = { AppBottomBars(selectedIndex, items) { selectedIndex = it } }
            ) { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding()
                val bottomPadding = paddingValues.calculateBottomPadding()

                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = items[selectedIndex].route,
                    modifier = Modifier
                        .padding(top = topPadding, bottom = bottomPadding)
                        .fillMaxSize()
                ) {
                    composable(RouteConfig.HOME) { HomePage() }
                    composable(RouteConfig.TOOL) { ToolPage() }
                    composable(RouteConfig.MORE) { MorePage() }
                }

                navController.navigateSingleTop(items[selectedIndex].route)

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
}