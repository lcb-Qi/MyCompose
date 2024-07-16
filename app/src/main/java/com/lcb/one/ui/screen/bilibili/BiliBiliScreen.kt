package com.lcb.one.ui.screen.bilibili

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.localization.Localization
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.AppNavGraph
import com.lcb.one.ui.screen.bilibili.repo.BiliServerAccessor
import com.lcb.one.ui.screen.bilibili.widget.BigImageViewer
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.common.DateTimeUtils
import kotlinx.coroutines.launch
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalSharedTransitionApi::class)
@Destination<AppNavGraph>
@Composable
fun BiliBiliScreen() {

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
                coverUrl = BiliServerAccessor.getVideoCoverUrl(textInput)
            }
        } else {
            AppGlobalConfigs.assertNetwork = true
        }
    }

    val download: () -> Unit = {
        scope.launch {
            StorageUtils.createImageFromUrl(coverUrl, DateTimeUtils.nowStringShort())
                .onSuccess { ToastUtils.showToast("${Localization.saveSuccess} $it") }
                .onFailure { ToastUtils.showToast(Localization.saveFailed) }
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { ToolBar(title = Localization.bibibili) },
        floatingActionButton = {
            if (coverUrl.isNotBlank()) {
                FloatingActionButton(onClick = download) {
                    Icon(imageVector = Icons.Rounded.Download, contentDescription = "")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                singleLine = true,
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text(text = Localization.getCoverHint) },
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(text = Localization.obtain, onClick = getCoverUrl)


            SubcomposeAsyncImage(
                modifier = Modifier
                    .width(240.dp)
                    .clickable { onShowBigImage(coverUrl) },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUrl)
                    .size(Size.ORIGINAL)
                    .placeholder(R.drawable.icon_bilibili)
                    .error(R.drawable.icon_bilibili)
                    .build(),
                contentDescription = null,
                loading = { CircularProgressIndicator() }
            )
        }
    }
}