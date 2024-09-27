package com.lcb.one.ui.screen.device

import android.os.Bundle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.device.repo.model.BasicInfo
import com.lcb.one.ui.screen.device.repo.model.DisplayInfo
import com.lcb.one.ui.screen.device.repo.model.toMap
import com.lcb.one.ui.screen.device.widget.SimpleInfoCard
import com.lcb.weight.appbar.ToolBar
import com.lcb.one.util.platform.Res
import kotlinx.coroutines.launch

object DeviceInfoScreen : Screen() {
    override val label: String = Res.string(R.string.device_info)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        Scaffold(topBar = { ToolBar(title = label) }) { innerPadding ->
            val tabTitles = listOf(stringResource(R.string.device), stringResource(R.string.display))
            val pagerState = rememberPagerState { tabTitles.size }
            val coroutineScope = rememberCoroutineScope()

            Column(modifier = Modifier.padding(innerPadding)) {
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
}



