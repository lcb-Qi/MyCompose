package com.lcb.one.ui.screen.device

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lcb.one.localization.Localization
import com.lcb.one.ui.screen.device.widget.BasicInfoList
import com.lcb.one.ui.screen.device.widget.DisplayInfoList
import com.lcb.one.ui.widget.appbar.ToolBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceInfoScreen() {
    Scaffold(topBar = { ToolBar(title = Localization.deviceInfo) }) { paddingValues ->
        val tabTitles = listOf(Localization.device, Localization.display)
        val pagerState = rememberPagerState { tabTitles.size }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                tabTitles.forEachIndexed { index, text ->
                    Tab(
                        modifier = Modifier.zIndex(2f),
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = text, style = MaterialTheme.typography.labelLarge) },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                when (it) {
                    0 -> BasicInfoList()
                    1 -> DisplayInfoList()
                }
            }
        }
    }
}


