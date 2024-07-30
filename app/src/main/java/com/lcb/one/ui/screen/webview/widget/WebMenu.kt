package com.lcb.one.ui.screen.webview.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lcb.one.R

sealed class WebAction {
    data object Refresh : WebAction()
    data object CopyUrl : WebAction()
    data object OpenInBrowser : WebAction()
}

@Composable
fun WebMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAction: (action: WebAction) -> Unit
) {
    val onMenuClick: (action: WebAction) -> Unit = {
        onDismiss()
        onAction(it)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        content = {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.open_in_your_browser)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null
                    )
                },
                onClick = { onMenuClick(WebAction.OpenInBrowser) }
            )

            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.copy_link)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Link,
                        contentDescription = null
                    )
                },
                onClick = { onMenuClick(WebAction.CopyUrl) }
            )

            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.refresh)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null
                    )
                },
                onClick = { onMenuClick(WebAction.Refresh) }
            )
        }
    )
}