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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.widget.appbar.BottomBar
import com.lcb.one.ui.widget.appbar.BottomBarItem
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.FriendlyExitHandler
import com.lcb.one.ui.widget.dialog.PoemInfoDialog
import com.lcb.one.util.android.AppUtils
import com.lcb.one.viewmodel.PoemViewModel

@Composable
fun MainScreen() {
    FriendlyExitHandler()

    val poemViewModel = viewModel<PoemViewModel>()
    val bottomItem = listOf(
        BottomBarItem(stringResource(R.string.home), Icons.Rounded.Home),
        BottomBarItem(stringResource(R.string.tool), Icons.Rounded.Android),
        BottomBarItem(stringResource(R.string.more), Icons.Rounded.MoreHoriz)
    )
    var showDetail by remember { mutableStateOf(false) }

    val poemInfo by poemViewModel.poemFlow.collectAsState()
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            ToolBar(
                title = poemInfo.recommend,
                onTitleClick = {
                    if (AppUtils.isNetworkAvailable()) {
                        poemViewModel.refresh(true)
                    } else {
                        AppGlobalConfigs.assertNetwork = true
                    }
                },
                onTitleLongClick = { showDetail = true },
                enableBack = false
            )
        },
        bottomBar = {
            BottomBar(currentIndex, bottomItem) { currentIndex = it }
        }
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
    }

    PoemInfoDialog(
        showDetail,
        title = poemInfo.origin.title,
        authorInfo = "${poemInfo.origin.dynasty} ${poemInfo.origin.author}",
        content = poemInfo.origin.content,
        onDismissRequest = { showDetail = false },
        onConfirm = { showDetail = false }
    )
    // TODO: 仅在app启动时执行一次
    if (AppUtils.isNetworkAvailable()) {
        poemViewModel.refresh(false)
    }
}