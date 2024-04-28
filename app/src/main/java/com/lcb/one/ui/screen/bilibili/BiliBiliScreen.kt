package com.lcb.one.ui.screen.bilibili

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.panpf.zoomimage.CoilZoomAsyncImage
import com.github.panpf.zoomimage.ZoomImage
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.screen.bilibili.repo.BiliServerAccessor
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

@Composable
fun BiliBiliScreen() {
    var textInput by remember { mutableStateOf("") }
    var coverUrl by remember { mutableStateOf("") }
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

            Button(onClick = getCoverUrl, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.obtain))
            }

            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUrl)
                    .placeholder(R.drawable.icon_bilibili)
                    .error(R.drawable.icon_bilibili)
                    .build(),
                contentDescription = "",
                loading = { CircularProgressIndicator() }
            )
        }
    }
}