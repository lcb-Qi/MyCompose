package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lcb.one.R

@Composable
fun SimpleMessageDialog(
    show: Boolean,
    title: String? = null,
    message: String? = null,
    icon: @Composable (() -> Unit)? = null,
    confirmText: String = stringResource(R.string.ok),
    cancelText: String = stringResource(R.string.cancel),
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onCancel,
        icon = icon,
        title = if (title.isNullOrBlank()) {
            null
        } else {
            {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        text = if (message.isNullOrBlank()) {
            null
        } else {
            {
                Text(
                    text = message,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = cancelText)
            }
        }
    )
}