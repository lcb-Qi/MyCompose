package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.lcb.one.localization.Localization
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun SimpleMessageDialog(
    show: Boolean,
    title: String? = null,
    message: String? = null,
    icon: @Composable (() -> Unit)? = null,
    confirmText: String = Localization.ok,
    cancelText: String = Localization.cancel,
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
        confirmButton = { AppTextButton(text = confirmText, onClick = onConfirm) },
        dismissButton = { AppTextButton(text = cancelText, onClick = onCancel) }
    )
}