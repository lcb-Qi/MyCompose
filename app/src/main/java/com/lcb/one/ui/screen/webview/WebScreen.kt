package com.lcb.one.ui.screen.webview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.webview.widget.WebAction
import com.lcb.one.ui.screen.webview.widget.WebMenu
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.ClipboardUtils

object WebScreen : Screen {
    override val route: String
        get() = "Web?url={url}"

    override val args: List<NamedNavArgument>
        get() = listOf(navArgument("url") { type = NavType.StringType })

    // 使用路径参数会提示找不到deeplink，这里改为可选参数
    fun createRoute(url: String) = "Web?url=$url"

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun Content(args: Bundle?) {
        val url = args?.getString("url") ?: ""
        val navController = LocalNav.current!!
        val webViewState = rememberWebViewState(url)
        val webNav = rememberWebViewNavigator()
        Scaffold(
            topBar = {
                ToolBar(
                    navIcon = {
                        AppIconButton(
                            icon = Icons.Rounded.Close,
                            onClick = { navController.popBackStack() }
                        )
                    },
                    title = webViewState.pageTitle ?: "",
                    titleStyle = MaterialTheme.typography.titleSmall,
                    actions = {
                        var showMenu by remember { mutableStateOf(false) }
                        AppIconButton(icon = Icons.Rounded.MoreVert, onClick = { showMenu = true })
                        val handleMenuAction: (WebAction) -> Unit = { action ->
                            val lastUrl = webViewState.lastLoadedUrl ?: ""
                            when (action) {
                                WebAction.CopyUrl -> ClipboardUtils.copyText(text = lastUrl)
                                WebAction.OpenInBrowser -> AppUtils.launchSystemBrowser(uri = lastUrl.toUri())
                                // reload会丢失标题
                                WebAction.Refresh -> webNav.loadUrl(lastUrl)
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
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                ProgressIndicator(loadingState = webViewState.loadingState)
                WebView(
                    state = webViewState,
                    navigator = webNav,
                    onCreated = {
                        it.settings.javaScriptEnabled = true
                    }
                )
            }
        }
    }

    @Composable
    private fun ProgressIndicator(modifier: Modifier = Modifier, loadingState: LoadingState) {
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth(),
                progress = { loadingState.progress },
                drawStopIndicator = {}
            )
        }
    }
}