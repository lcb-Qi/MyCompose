package com.lcb.one.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lcb.one.R
import com.lcb.one.bean.BasicInfo
import com.lcb.one.bean.DisplayInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceInfoScreen() {
    val tabTitles = listOf(stringResource(R.string.device), stringResource(R.string.display))
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
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
            // selectedIndex = it
            when (it) {
                0 -> BasicInfoList()
                1 -> DisplayInfoList()
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

@Composable
private fun DisplayInfoList() {
    val displayInfo by remember { mutableStateOf(DisplayInfo.obtain()) }

    InfoList(
        mapOf(
            "Resolution" to displayInfo.resolution.toString() + " px",
            "Display Size" to displayInfo.displaySize.toString() + " px",
            "Small Width" to displayInfo.smallWidth.toString() + " dp",
            "DPI" to displayInfo.dpi.toString(),
            "Density" to displayInfo.density.toString(),
            "StatusBars Height" to displayInfo.statusBarsHeight.toString() + " px",
            "NavigationBars Height" to displayInfo.navigationBarsHeight.toString() + " px",
        )
    )
}

@Composable
private fun BasicInfoList() {
    val deviceInfo by remember { mutableStateOf(BasicInfo.obtain()) }

    InfoList(
        mapOf(
            "Device Model" to deviceInfo.deviceModel,
            "Brand" to deviceInfo.brand,
            "OS Version" to deviceInfo.osVersion,
            "SDK Version" to deviceInfo.sdkVersion.toString()
        )
    )
}


