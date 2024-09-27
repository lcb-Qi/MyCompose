package com.lcb.one.ui.screen.main

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.main.home.HomePage
import com.lcb.one.ui.screen.main.settings.SettingsPage
import com.lcb.weight.appbar.BottomBar
import com.lcb.weight.appbar.BottomBarItem
import com.lcb.weight.appbar.ToolBar
import com.lcb.one.ui.screen.main.tool.ToolPage
import com.lcb.one.util.platform.Res
import kotlinx.coroutines.launch


object MainScreen : Screen() {
    override val label: String = Res.string(R.string.main_screen)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        val bottomItem = remember(Unit) {
            listOf(
                BottomBarItem(Res.string(R.string.home), Icons.Rounded.Home),
                BottomBarItem(Res.string(R.string.tool), Icons.Rounded.Android),
                BottomBarItem(Res.string(R.string.settings), Icons.Rounded.Settings)
            )
        }

        val pagerState = rememberPagerState { bottomItem.size }
        val scope = rememberCoroutineScope()
        val title by remember {
            derivedStateOf { bottomItem[pagerState.currentPage].label }
        }
        Scaffold(
            topBar = { ToolBar(title = title, navIcon = null) },
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
                    0 -> HomePage(navController)
                    1 -> ToolPage(navController)
                    2 -> SettingsPage(navController)
                }
            }
        }
    }
}