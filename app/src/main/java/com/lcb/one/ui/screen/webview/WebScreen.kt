package com.lcb.one.ui.screen.webview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.lcb.one.ui.AppNavGraph
import com.lcb.one.ui.navController
import com.lcb.one.ui.screen.webview.widget.WebAction
import com.lcb.one.ui.screen.webview.widget.WebMenu
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.ClipboardUtils
import com.ramcosta.composedestinations.annotation.Destination

@Destination<AppNavGraph>
@Composable
fun WebScreen(url: String) {
    val webViewState = rememberWebViewState(url = url)
    val webNav = rememberWebViewNavigator()
    Scaffold(
        topBar = {
            ToolBar(
                navigationIcon = {
                    AppIconButton(
                        icon = Icons.Rounded.Close,
                        onClick = { navController.popBackStack() }
                    )
                },
                title = webViewState.pageTitle ?: "",
                titleStyle = MaterialTheme.typography.titleMedium,
                actions = {
                    var showMenu by remember { mutableStateOf(false) }
                    AppIconButton(icon = Icons.Rounded.MoreVert, onClick = { showMenu = true })
                    val handleMenuAction: (WebAction) -> Unit = { action ->
                        showMenu = false
                        webViewState.lastLoadedUrl?.let {
                            when (action) {
                                WebAction.CopyUrl -> ClipboardUtils.copyText(text = it)
                                WebAction.OpenInBrowser -> AppUtils.launchSystemBrowser(uri = it.toUri())
                                WebAction.Refresh -> webNav.loadUrl(it)
                            }
                        }
                    }
                    WebMenu(
                        expanded = showMenu,
                        onDismiss = { showMenu = false },
                        onAction = handleMenuAction
                    )
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val loadingState = webViewState.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { loadingState.progress }
                )
            }
            WebView(state = webViewState, navigator = webNav)
        }
    }
}