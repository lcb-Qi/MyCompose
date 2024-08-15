package com.lcb.one.ui.screen.bilibili.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import coil.size.Size
import com.github.panpf.zoomimage.CoilZoomAsyncImage
import com.lcb.one.R

@Composable
fun BigImageViewer(modifier: Modifier = Modifier, url: String, onBack: () -> Unit) {
    BackHandler(onBack = onBack)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseSurface)
            .clickable { onBack() }
    ) {
        CoilZoomAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .size(Size.ORIGINAL)
                .build(),
            scrollBar = null,
            contentDescription = null,
            onTap = { onBack() }
        )

        var downloading by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick = { downloading = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 32.dp),
            content = {
                Text(
                    text = stringResource(R.string.save),
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        )

        DownloadDialog(downloading, url) { downloading = false }
    }
}