package com.lcb.one.ui.screen.bilibili.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.panpf.zoomimage.CoilZoomAsyncImage
import com.lcb.one.localization.Localization

@Composable
fun BigImageViewer(
    modifier: Modifier = Modifier,
    url: String,
    onSave: () -> Unit = {},
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onBack() },
        content = {
            CoilZoomAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                model = url,
                scrollBar = null,
                contentDescription = null,
                onTap = { onBack() }
            )

            OutlinedButton(
                onClick = onSave,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp),
                content = { Text(text = Localization.save) }
            )
        }
    )
}