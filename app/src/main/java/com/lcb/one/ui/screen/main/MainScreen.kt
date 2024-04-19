package com.lcb.one.ui.screen.main

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.widget.appbar.BottomBar
import com.lcb.one.ui.widget.appbar.BottomBarItem
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.FriendlyExitHandler
import com.lcb.one.ui.screen.main.widget.PoemInfoDialog
import com.lcb.one.util.android.AppUtils
import com.lcb.one.ui.screen.main.repo.MainViewModel
import com.lcb.one.ui.screen.main.repo.MainViewModel.Event
import com.lcb.one.ui.widget.dialog.LoadingDialog

@Composable
fun MainScreen() {
    FriendlyExitHandler()

    val mainViewModel = viewModel<MainViewModel>()
    val poemState by mainViewModel.poemSate.collectAsState()

    val bottomItem = listOf(
        BottomBarItem(stringResource(R.string.home), Icons.Rounded.Home),
        BottomBarItem(stringResource(R.string.tool), Icons.Rounded.Android),
        BottomBarItem(stringResource(R.string.more), Icons.Rounded.MoreHoriz)
    )

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            ToolBar(
                title = poemState.poemInfo.recommend,
                onTitleClick = {
                    if (AppUtils.isNetworkAvailable()) {
                        mainViewModel.sendEvent(Event.Refresh(true))
                    } else {
                        AppGlobalConfigs.assertNetwork = true
                    }
                },
                onTitleLongClick = { mainViewModel.sendEvent(Event.ShowDetail) },
                enableBack = false
            )
        },
        bottomBar = { BottomBar(currentIndex, bottomItem) { currentIndex = it } }
    ) { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding, bottom = bottomPadding)
        ) {
            when (currentIndex) {
                0 -> HomeScreen()
                1 -> ToolScreen()
                2 -> MoreScreen()
            }
        }

        PoemInfoDialog(
            show = poemState.showDetail,
            origin = poemState.poemInfo.origin,
            onDismiss = {
                mainViewModel.sendEvent(Event.HideDetail)
            }
        )

        LoadingDialog(poemState.loading)
    }
    // TODO: 仅在app启动时执行一次
    if (AppUtils.isNetworkAvailable()) {
        mainViewModel.sendEvent(Event.Refresh(false))
    }
}