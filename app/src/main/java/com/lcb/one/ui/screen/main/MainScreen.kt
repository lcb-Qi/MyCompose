package com.lcb.one.ui.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.BottomBar
import com.lcb.one.ui.widget.appbar.BottomBarItem
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.FriendlyExitHandler
import com.lcb.one.ui.screen.main.widget.PoemInfoDialog
import com.lcb.one.util.android.AppUtils
import com.lcb.one.ui.screen.main.repo.MainViewModel
import com.lcb.one.ui.screen.main.repo.MainViewModel.Event
import com.lcb.one.ui.screen.main.widget.PoemTitle
import com.lcb.one.util.android.navigateSingleTop
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val navHostController = LocalNav.current!!

    FriendlyExitHandler()

    val mainViewModel = viewModel<MainViewModel>()
    val poemState = mainViewModel.poemSate

    val bottomItem = listOf(
        BottomBarItem(Localization.home, Icons.Rounded.Home),
        BottomBarItem(Localization.tool, Icons.Rounded.Android),
        BottomBarItem(Localization.more, Icons.Rounded.MoreHoriz)
    )

    val pagerState = rememberPagerState { bottomItem.size }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            val updatePoem: () -> Unit = {
                if (AppUtils.isNetworkAvailable()) {
                    mainViewModel.sendEvent(Event.Refresh(true))
                } else {
                    AppGlobalConfigs.assertNetwork = true
                }
            }

            val showDetail: () -> Unit = { mainViewModel.sendEvent(Event.ShowDetail(true)) }

            ToolBar(
                title = {
                    PoemTitle(
                        poem = poemState.poemInfo.recommend,
                        onLongClick = showDetail,
                        onClick = updatePoem
                    )
                },
                enableBack = false,
                actions = {
                    IconButton(onClick = { navHostController.navigateSingleTop(Route.THEME) }) {
                        Icon(Icons.Rounded.Palette, null)
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = pagerState.currentPage,
                items = bottomItem,
                onItemChanged = { scope.launch { pagerState.animateScrollToPage(it) } }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) {
            when (it) {
                0 -> HomeScreen()
                1 -> ToolScreen()
                2 -> MoreScreen()
            }
        }

        PoemInfoDialog(
            show = poemState.showDetail,
            origin = poemState.poemInfo.origin,
            onDismiss = {
                mainViewModel.sendEvent(Event.ShowDetail(false))
            }
        )
    }
    // TODO: 仅在app启动时执行一次
    if (AppUtils.isNetworkAvailable()) {
        mainViewModel.sendEvent(Event.Refresh(false))
    }
}