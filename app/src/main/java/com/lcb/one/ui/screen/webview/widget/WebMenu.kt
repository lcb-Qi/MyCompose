package com.lcb.one.ui.screen.webview.widget

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.lcb.one.ui.MyApp
import com.lcb.one.util.android.ClipboardUtils

sealed class WebAction {
    data object Refresh : WebAction()
    data object CopyUrl : WebAction()
    data object OpenInBrowser : WebAction()
}

@Composable
fun WebMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAction: (action: WebAction) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        content = {
            DropdownMenuItem(
                text = { Text(text = "在浏览器中打开") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null
                    )
                },
                onClick = { onAction(WebAction.OpenInBrowser) }
            )

            DropdownMenuItem(
                text = { Text(text = "复制链接") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Link,
                        contentDescription = null
                    )
                },
                onClick = { onAction(WebAction.CopyUrl) }
            )

            DropdownMenuItem(
                text = { Text(text = "刷新") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null
                    )
                },
                onClick = { onAction(WebAction.Refresh) }
            )
        }
    )
}