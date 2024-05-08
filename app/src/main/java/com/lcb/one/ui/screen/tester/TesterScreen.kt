package com.lcb.one.ui.screen.tester

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.screen.tester.widget.ColorSchemeDialog
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TesterScreen() {
    Scaffold(topBar = { ToolBar(title = "Tester") }) { paddingValues ->
        var showColorScheme by remember { mutableStateOf(false) }
        FlowRow(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            AppButton(text = "检查主题色", onClick = { showColorScheme = true })
        }

        ColorSchemeDialog(showColorScheme) { showColorScheme = false }
    }
}