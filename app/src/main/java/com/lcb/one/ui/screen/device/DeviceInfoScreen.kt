package com.lcb.one.ui.screen.device

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppNavGraph
import com.lcb.one.ui.screen.device.repo.model.BasicInfo
import com.lcb.one.ui.screen.device.repo.model.DisplayInfo
import com.lcb.one.ui.screen.device.repo.model.toMap
import com.lcb.one.ui.screen.device.widget.SimpleInfoCard
import com.lcb.one.ui.widget.appbar.ToolBar
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination<AppNavGraph>
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen() {
    Scaffold(topBar = { ToolBar(title = Localization.deviceInfo) }) { paddingValues ->
        val tabTitles = listOf(Localization.device, Localization.display)
        val pagerState = rememberPagerState { tabTitles.size }
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier.padding(paddingValues)) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                tabTitles.forEachIndexed { index, text ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = text) },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    val deviceInfo = remember { BasicInfo.obtain().toMap() }
                    val displayInfo = remember { DisplayInfo.obtain().toMap() }
                    when (it) {
                        0 -> SimpleInfoCard(data = deviceInfo)
                        1 -> SimpleInfoCard(data = displayInfo)
                    }
                }
            }
        }
    }
}


