package com.lcb.one.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lcb.one.R
import com.lcb.one.ui.page.HomePage
import com.lcb.one.ui.page.MorePage
import com.lcb.one.viewmodel.PoemViewModel
import com.lcb.one.ui.page.ToolPage
import com.lcb.one.ui.widget.TopAppBars
import com.lcb.one.ui.widget.AppThemeSurface
import com.lcb.one.ui.widget.BottomBars
import com.lcb.one.ui.widget.BottomBarItem
import com.lcb.one.ui.widget.dialog.PoemInfoDialog

class HomeActivity : ComponentActivity() {

    private val poemViewModel by viewModels<PoemViewModel>()

    private val items by lazy {
        listOf(
            BottomBarItem(0, getString(R.string.home), Icons.Filled.Home),
            BottomBarItem(1, getString(R.string.tool), Icons.Filled.Android),
            BottomBarItem(2, getString(R.string.more), Icons.Filled.MoreHoriz)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppScreen() }
    }

    override fun onResume() {
        super.onResume()
        poemViewModel.getPoem(false)
    }

    @Composable
    private fun AppScreen() {
        AppThemeSurface {
            var selectedIndex by remember { mutableIntStateOf(0) }

            val poemInfo by poemViewModel.poemFlow.collectAsState()
            var showDetail by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    TopAppBars(
                        text = poemInfo.recommend,
                        onClick = { poemViewModel.getPoem(true) },
                        onLongClick = { showDetail = true }
                    )
                },
                bottomBar = { BottomBars(selectedIndex, items) { selectedIndex = it } }
            ) { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding()
                val bottomPadding = paddingValues.calculateBottomPadding()
                Column(
                    modifier = Modifier
                        .padding(top = topPadding, bottom = bottomPadding)
                        .fillMaxSize()
                ) {
                    when (selectedIndex) {
                        // 带有 NavHost 的页面，需要占满全部空间，否则动画不生效，原因未知
                        // 这里通过 Column 设置 weight = 1，fill = true
                        0 -> HomePage(Modifier.weight(1f))
                        1 -> ToolPage(Modifier.weight(1f))
                        2 -> MorePage(Modifier.weight(1f))
                    }
                }

                if (showDetail) {
                    PoemInfoDialog(
                        poemDetail = poemInfo.origin,
                        onDismissRequest = { showDetail = false },
                        onConfirm = { showDetail = false }
                    )
                }
            }
        }
    }
}

