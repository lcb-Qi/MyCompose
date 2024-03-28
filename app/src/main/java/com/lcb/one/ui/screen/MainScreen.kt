package com.lcb.one.ui.screen

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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.widget.AppBottomBars
import com.lcb.one.ui.widget.BottomBarItem
import com.lcb.one.ui.widget.AppBar
import com.lcb.one.ui.widget.dialog.PoemInfoDialog
import com.lcb.one.viewmodel.PoemViewModel

@Composable
fun MainScreen(navController: NavHostController) {
    val poemViewModel = viewModel<PoemViewModel>()
    val bottomItem = listOf(
        BottomBarItem(stringResource(R.string.home), Icons.Rounded.Home),
        BottomBarItem(stringResource(R.string.tool), Icons.Rounded.Android),
        BottomBarItem(stringResource(R.string.more), Icons.Rounded.MoreHoriz)
    )
    var showDetail by remember { mutableStateOf(false) }

    val poemInfo by poemViewModel.poemFlow.collectAsState()
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            AppBar(
                title = poemInfo.recommend,
                onTitleClick = { poemViewModel.refresh(true) },
                onTitleLongClick = { showDetail = true },
                enableBack = false
            )
        },
        bottomBar = {
            AppBottomBars(currentIndex, bottomItem) { currentIndex = it }
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
                1 -> ToolScreen(navController)
                2 -> MoreScreen(navController)
            }
        }
    }

    PoemInfoDialog(
        showDetail,
        poemDetail = poemInfo.origin,
        onDismissRequest = { showDetail = false },
        onConfirm = { showDetail = false }
    )
    // TODO: 仅在app启动时执行一次
    poemViewModel.refresh(false)
}