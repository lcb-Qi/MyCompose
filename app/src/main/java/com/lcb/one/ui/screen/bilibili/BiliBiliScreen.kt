package com.lcb.one.ui.screen.bilibili

import android.os.Bundle
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.bilibili.repo.BiliServerHelper
import com.lcb.one.ui.screen.bilibili.widget.BigImageViewer
import com.lcb.one.ui.screen.bilibili.widget.DownloadDialog
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.Res
import kotlinx.coroutines.launch

object BiliBiliScreen : Screen() {
    override val label: String = Res.string(R.string.bibibili)

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            var showBig by remember { mutableStateOf(false) }
            var url by remember { mutableStateOf("") }

            AnimatedContent(
                targetState = showBig,
                label = "",
            ) { targetState ->
                if (targetState) {
                    BigImageViewer(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = "big"),
                            animatedVisibilityScope = this@AnimatedContent,
                        ),
                        url = url,
                        onBack = { showBig = false },
                    )
                } else {
                    BiliScreenImpl(
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "small"),
                                animatedVisibilityScope = this@AnimatedContent,
                            ),
                        onShowBigImage = {
                            showBig = true
                            url = it
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun BiliScreenImpl(
        modifier: Modifier = Modifier,
        onShowBigImage: (url: String) -> Unit = {},
    ) {
        var textInput by remember { mutableStateOf("") }
        var coverUrl by remember { mutableStateOf("") }
        if (BuildConfig.DEBUG) {
            coverUrl =
                "https://patchwiki.biligame.com/images/sr/b/be/crncyly4kxzqik8h4uc98aj1c8vovbe.png"
        }
        val scope = rememberCoroutineScope()

        val getCoverUrl: () -> Unit = {
            if (AppUtils.isNetworkAvailable()) {
                if (BuildConfig.DEBUG) textInput = "BV1Jp421y768"
                scope.launch {
                    coverUrl = BiliServerHelper.getVideoCoverUrl(textInput)
                }
            } else {
                AppGlobalConfigs.assertNetwork = true
            }
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { ToolBar(title = label) },
            floatingActionButton = {
                var downloading by remember { mutableStateOf(false) }
                if (coverUrl.isNotBlank()) {
                    FloatingActionButton(
                        onClick = { downloading = true },
                        content = { Icon(Icons.Rounded.Download, null) }
                    )
                }
                DownloadDialog(downloading, coverUrl) { downloading = false }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    singleLine = true,
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text(text = stringResource(R.string.get_cover_hint)) },
                    modifier = Modifier.fillMaxWidth()
                )

                AppButton(text = stringResource(R.string.obtain), onClick = getCoverUrl)


                SubcomposeAsyncImage(
                    modifier = Modifier
                        .width(240.dp)
                        .clickable { onShowBigImage(coverUrl) },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(coverUrl)
                        .placeholder(R.drawable.icon_bilibili)
                        .error(R.drawable.icon_bilibili)
                        .build(),
                    contentDescription = null,
                    loading = {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                )
            }
        }
    }
}