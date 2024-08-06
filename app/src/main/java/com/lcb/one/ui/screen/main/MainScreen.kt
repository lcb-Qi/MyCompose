package com.lcb.one.ui.screen.main

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.main.home.HomeScreen
import com.lcb.one.ui.screen.main.more.MoreScreen
import com.lcb.one.ui.widget.appbar.BottomBar
import com.lcb.one.ui.widget.appbar.BottomBarItem
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.FriendlyExitHandler
import com.lcb.one.ui.screen.main.widget.PoemInfoDialog
import com.lcb.one.util.android.AppUtils
import com.lcb.one.ui.screen.main.repo.MainViewModel
import com.lcb.one.ui.screen.main.tool.ToolScreen
import com.lcb.one.ui.widget.common.noRippleClickable
import com.lcb.one.util.android.Res
import kotlinx.coroutines.launch


object MainScreen : Screen {
    override val route: String
        get() = "Main"

    override val label: String
        get() = Res.string(R.string.main_screen)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        // FriendlyExitHandler()

        val mainViewModel = viewModel<MainViewModel>()
        val poemState by mainViewModel.poemInfo.collectAsState()
        var showDetail by remember { mutableStateOf(false) }

        val bottomItem = remember(Unit) {
            listOf(
                BottomBarItem(Res.string(R.string.home), Icons.Rounded.Home),
                BottomBarItem(Res.string(R.string.tool), Icons.Rounded.Android),
                BottomBarItem(Res.string(R.string.more), Icons.Rounded.MoreHoriz)
            )
        }

        val pagerState = rememberPagerState { bottomItem.size }
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                val updatePoem: () -> Unit = {
                    if (AppUtils.isNetworkAvailable()) {
                        mainViewModel.updatePoem(true)
                    } else {
                        AppGlobalConfigs.assertNetwork = true
                    }
                }

                ToolBar(
                    title = {
                        Text(
                            text = poemState.recommend,
                            modifier = Modifier
                                .noRippleClickable(
                                    onLongClick = { showDetail = true },
                                    onClick = updatePoem
                                ),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navIcon = null,
                )
            },
            bottomBar = {
                BottomBar(
                    selectedIndex = pagerState.currentPage,
                    items = bottomItem,
                    onItemChanged = { scope.launch { pagerState.animateScrollToPage(it) } }
                )
            }
        ) { innerPadding ->
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) {
                when (it) {
                    0 -> HomeScreen(navController)
                    1 -> ToolScreen(navController)
                    2 -> MoreScreen(navController)
                }
            }

            PoemInfoDialog(
                show = showDetail,
                origin = { poemState.origin },
                onDismiss = { showDetail = false }
            )
        }
        // TODO: 仅在app启动时执行一次
        if (AppUtils.isNetworkAvailable()) {
            mainViewModel.updatePoem(false)
        }
    }
}