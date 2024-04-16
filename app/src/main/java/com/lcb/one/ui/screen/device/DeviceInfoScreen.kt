package com.lcb.one.ui.screen.device

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lcb.one.R
import com.lcb.one.ui.screen.device.widget.BasicInfoList
import com.lcb.one.ui.screen.device.widget.DisplayInfoList
import com.lcb.one.ui.widget.appbar.ToolBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceInfoScreen() {
    Scaffold(topBar = { ToolBar(title = stringResource(R.string.device_info)) }) { paddingValues ->
        val tabTitles = listOf(stringResource(R.string.device), stringResource(R.string.display))
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

@Composable
private fun InfoList(items: Map<String, String>) {
    LazyColumn {
        items.forEach { (key, value) ->
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }
}


