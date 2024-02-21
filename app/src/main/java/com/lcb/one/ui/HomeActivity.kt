package com.lcb.one.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.page.HomePage
import com.lcb.one.viewmodel.PoemViewModel
import com.lcb.one.ui.page.SettingsPage
import com.lcb.one.ui.page.ToolPage
import com.lcb.one.ui.theme.labelLarge
import com.lcb.one.ui.theme.primaryColor
import com.lcb.one.ui.theme.primaryContainerColor
import com.lcb.one.ui.widget.AppActionBar
import com.lcb.one.ui.widget.AppThemeSurface

class HomeActivity : ComponentActivity() {

    private val poemViewModel by viewModels<PoemViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppView() }
    }

    override fun onResume() {
        super.onResume()
        poemViewModel.getPoem(false)
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun AppView() {
        AppThemeSurface {
            Scaffold(topBar = { AppActionBar() }) { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding()
                val bottomPadding = paddingValues.calculateBottomPadding()
                Column(
                    modifier = Modifier
                        .padding(top = topPadding, bottom = bottomPadding)
                        .fillMaxSize()
                ) {
                    val items = listOf(
                        TabItem("主页", Icons.Filled.Home),
                        TabItem("工具", Icons.Filled.List),
                        TabItem("设置", Icons.Filled.Settings)
                    )
                    var selectedIndex by remember { mutableIntStateOf(0) }

                    when (selectedIndex) {
                        0 -> HomePage(modifier = Modifier.weight(1f).wrapContentSize())
                        1 -> ToolPage(modifier = Modifier.weight(1f).wrapContentSize())
                        2 -> SettingsPage(modifier = Modifier.weight(1f).wrapContentSize())
                    }

                    AppTab(selectedIndex, items) {
                        selectedIndex = it
                    }
                }
            }
        }
    }

    @Composable
    private fun AppTab(selectedIndex: Int, items: List<TabItem>, onClick: (Int) -> Unit) {
        TabRow(
            divider = {},
            containerColor = primaryContainerColor(),
            selectedTabIndex = selectedIndex,
            indicator = {}
        ) {
            items.forEachIndexed { index, tabItem ->
                val selected = selectedIndex == index
                val indicatorColor = if (selected) primaryColor() else Color.Gray
                Tab(
                    icon = { Icon(tabItem.icon, "", tint = indicatorColor) },
                    selected = selected,
                    onClick = { onClick(index) },
                    text = { Text(tabItem.title, color = indicatorColor, style = labelLarge()) },
                )
            }
        }
    }

    data class TabItem(val title: String, val icon: ImageVector)
}

