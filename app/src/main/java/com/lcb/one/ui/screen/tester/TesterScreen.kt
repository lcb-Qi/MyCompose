package com.lcb.one.ui.screen.tester

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Route
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.util.android.navigateSingleTop

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TesterScreen() {
    val navHostController = LocalNav.current!!
    Scaffold(topBar = { ToolBar(title = "Tester") }) { paddingValues ->
        FlowRow(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {

        }
    }
}