package com.lcb.one.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
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
import com.lcb.one.ui.widget.TopAppBars
import com.lcb.one.ui.widget.AppThemeSurface
import com.lcb.one.ui.widget.BottomBars
import com.lcb.one.ui.widget.BottomBarItem
import com.lcb.one.ui.widget.dialog.LoadingDialog
import com.lcb.one.ui.widget.dialog.PoemInfoDialog
import kotlinx.coroutines.delay
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
        lifecycleScope.launch {
            PoemAppWidget().updateAll(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        poemViewModel.refresh(false)
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
                bottomBar = { BottomBars(selectedIndex, items) { selectedIndex = it } }
            ) { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding()
                val bottomPadding = paddingValues.calculateBottomPadding()

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = items[selectedIndex].route,
                    enterTransition = { slideInHorizontally(animationSpec = tween(500)) { it } },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(500)) { -it } },
                    popEnterTransition = { slideInHorizontally(animationSpec = tween(500)) { -it } },
                    popExitTransition = { slideOutHorizontally(animationSpec = tween(500)) { it } },
                    modifier = Modifier
                        .padding(top = topPadding, bottom = bottomPadding)
                        .fillMaxSize()
                ) {
                    composable(RouteConfig.HOME) { HomePage() }
                    composable(RouteConfig.TOOL) { ToolPage() }
                    composable(RouteConfig.MORE) { MorePage() }
                }

                navController.navigateSingleTop(items[selectedIndex].route)

                if (showDetail) {
                    PoemInfoDialog(
                        poemDetail = poemInfo.origin,
                        onDismissRequest = { showDetail = false },
                        onConfirm = { showDetail = false }
                    )
                }
                if (isLoading) {
                    LoadingDialog {
                        Text(text = stringResource(R.string.loading))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ss() {
    Column {
        Text(
            text = "一二三四五，六七八九十",
            style = TextStyle(textAlign = TextAlign.Start),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "—— 你好 你好呀",
            style = TextStyle(textAlign = TextAlign.End),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

