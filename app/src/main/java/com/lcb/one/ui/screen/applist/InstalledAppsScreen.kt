package com.lcb.one.ui.screen.applist

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.ANIMATE_DURATION
import com.lcb.one.ui.screen.applist.repo.AppInfo
import com.lcb.one.ui.screen.applist.repo.loadInstalledApps
import com.lcb.one.ui.screen.applist.widget.AppType
import com.lcb.one.ui.screen.applist.widget.AppTypeSelector
import com.lcb.one.ui.screen.applist.widget.AppInfoDialog
import com.lcb.one.ui.screen.applist.widget.InstalledAppList
import com.lcb.weight.appbar.ToolBar
import com.lcb.weight.AppIconButton
import com.lcb.one.util.platform.Res
import com.lcb.one.util.platform.ToastUtils
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

object InstalledAppsScreen : Screen() {
    override val label: String = Res.string(R.string.app_list)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        var appType by remember { mutableStateOf(AppType.USER) }
        var loadSuccess by remember { mutableStateOf(false) }
        var allApps by remember { mutableStateOf(emptyList<AppInfo>()) }

        LaunchedEffect(Unit) {
            if (loadSuccess) return@LaunchedEffect
            delay(ANIMATE_DURATION.toLong())
            val costTime = measureTimeMillis { allApps = loadInstalledApps() }
            loadSuccess = true
            val msg = Res.string(R.string.load_success_msg, allApps.size, costTime)
            ToastUtils.send(msg)
        }

        Scaffold(
            topBar = {
                ToolBar(
                    title = label,
                    actions = {
                        var showTypeSelector by remember { mutableStateOf(false) }
                        AppIconButton(
                            enabled = loadSuccess,
                            icon = Icons.Rounded.MoreVert,
                            onClick = { showTypeSelector = true }
                        )
                        AppTypeSelector(
                            expanded = showTypeSelector,
                            offset = DpOffset(x = (-16).dp, y = 0.dp),
                            onSelected = {
                                showTypeSelector = false
                                appType = it
                            },
                            selectedType = appType,
                            onDismiss = { showTypeSelector = false }
                        )
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var showDetail by remember { mutableStateOf(false) }
                var packageName by remember { mutableStateOf("") }

                if (!loadSuccess) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    InstalledAppList(
                        apps = allApps,
                        displayType = appType,
                        onItemClick = {
                            packageName = it
                            showDetail = true
                        }
                    )
                }

                AppInfoDialog(showDetail, packageName) { showDetail = false }
            }
        }
    }
}
