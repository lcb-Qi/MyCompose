package com.lcb.one.ui.screen.applist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.appbar.ToolBar

@Composable
fun AppListScreen() {
    Scaffold(topBar = { ToolBar(title = "应用列表") }) { paddingValues ->
        var appSelection by remember { mutableStateOf(AppSelection.USER) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                var showDetail by remember { mutableStateOf(false) }
                TextButton(onClick = { showDetail = true }) {
                    Text(text = appSelection.label)
                }
                DropdownMenu(expanded = showDetail, onDismissRequest = { showDetail = false }) {
                    AppSelection.values().forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.label) },
                            onClick = {
                                showDetail = false
                                appSelection = it
                            }
                        )
                    }
                }
            }
            ApplicationList(appSelection = appSelection)
        }
    }
}
