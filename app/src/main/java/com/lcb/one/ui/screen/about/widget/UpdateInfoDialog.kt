package com.lcb.one.ui.screen.about.widget

import android.content.Intent
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.lcb.one.R
import com.lcb.one.ui.screen.about.repo.model.UpdateInfo
import com.lcb.one.ui.widget.common.AppTextButton
import io.noties.markwon.Markwon

@Composable
fun UpdateInfoDialog(show: Boolean, updateInfo: UpdateInfo?, onCancel: () -> Unit) {
    if (!show || updateInfo == null) return

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    AlertDialog(
        title = {
            Text(
                text = updateInfo.version.versionName,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            val markdown = Markwon.builder(LocalContext.current).build()
            AndroidView(
                factory = {
                    val textView = TextView(it)
                    markdown.setMarkdown(textView, updateInfo.message)
                    return@AndroidView textView
                },
                update = {
                    markdown.setMarkdown(it, updateInfo.message)
                }
            )
        },
        onDismissRequest = {},
        confirmButton = {
            AppTextButton(
                text = "去下载",
                onClick = {
                    launcher.launch(Intent(Intent.ACTION_VIEW, updateInfo.url.toUri()))
                }
            )
        },
        dismissButton = {
            AppTextButton(text = stringResource(R.string.cancel), onClick = onCancel)
        }
    )
}