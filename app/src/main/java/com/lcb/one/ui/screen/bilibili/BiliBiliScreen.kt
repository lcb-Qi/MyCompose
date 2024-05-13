package com.lcb.one.ui.screen.bilibili

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.screen.bilibili.repo.BiliServerAccessor
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch
import com.lcb.one.ui.screen.bilibili.widget.FullScreenImage
import com.lcb.one.ui.screen.bilibili.widget.ImageViewState
import kotlin.math.roundToInt

@Composable
fun BiliBiliScreen() {
    var textInput by remember { mutableStateOf("") }
    var coverUrl by remember { mutableStateOf("https://patchwiki.biligame.com/images/sr/b/be/crncyly4kxzqik8h4uc98aj1c8vovbe.png") }
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
            DownLoadUtil.saveImageFromUrl(coverUrl)
                .onSuccess { ToastUtils.showToast("保存成功 $it") }
                .onFailure { ToastUtils.showToast("保存失败") }
        }
    }

    var smallOffset = IntOffset.Zero
    var smallSize = IntSize.Zero

    val imageViewState by remember { mutableStateOf(ImageViewState()) }
    Scaffold(
        topBar = { ToolBar(title = stringResource(R.string.bibibili)) },
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
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "请输入av或BV号以获取封面",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                singleLine = true,
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text(text = "如BV117411r7R1") },
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(text = stringResource(R.string.obtain), onClick = getCoverUrl)

            SubcomposeAsyncImage(
                modifier = Modifier
                    .width(240.dp)
                    .clickable { imageViewState.doEnter(smallOffset, smallSize) }
                    .onGloballyPositioned {
                        val rect = it.boundsInRoot()
                        smallOffset = IntOffset(rect.left.roundToInt(), rect.top.roundToInt())
                        smallSize = it.size
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUrl)
                    .size(coil.size.Size.ORIGINAL)
                    .placeholder(R.drawable.icon_bilibili)
                    .error(R.drawable.icon_bilibili)
                    .build(),
                contentDescription = "",
                loading = { CircularProgressIndicator() }
            )
        }
    }

    FullScreenImage(
        url = coverUrl,
        state = imageViewState,
        onClick = { imageViewState.doExit(smallOffset, smallSize) }
    )
}