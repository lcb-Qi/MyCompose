package com.lcb.one.ui.screen.bilibili.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.screen.bilibili.repo.DownloadHelper
import com.lcb.one.util.android.Res
import com.lcb.one.util.android.ToastUtils

@Composable
fun DownloadDialog(show: Boolean, url: String, onFinish: (result: Result<String>) -> Unit) {
    if (!show) return

    val filename = remember(url) {
        url.substring(url.lastIndexOf("/") + 1)
    }
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        title = { Text(text = filename, style = MaterialTheme.typography.titleMedium) },
        text = {
            var downloadSize by remember { mutableLongStateOf(0L) }
            var totalSize by remember { mutableLongStateOf(0L) }

            LaunchedEffect(url) {
                val result = DownloadHelper.downloadFile(url, filename) { read, total ->
                    downloadSize = read
                    totalSize = total
                }

                result.onSuccess { ToastUtils.showToast("${Res.string(R.string.save_success)} $it") }
                    .onFailure { ToastUtils.showToast(Res.string(R.string.save_failed)) }

                onFinish(result)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val downloadProgress by remember(downloadSize, totalSize) {
                    mutableFloatStateOf(downloadSize.toFloat() / totalSize)
                }

                Row {
                    val text by remember(downloadSize, totalSize) {
                        mutableStateOf(
                            buildString {
                                append(DownloadHelper.friendlySize(downloadSize))
                                append(" / ")
                                append(DownloadHelper.friendlySize(totalSize))
                            }
                        )
                    }

                    Text(
                        text,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "%.02f%%".format(downloadProgress * 100),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { downloadProgress },
                    drawStopIndicator = {})
            }
        })
}