package com.lcb.weight.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lcb.weight.AppTextButton
import com.lcb.weight.R

@Composable
fun SimpleMessageDialog(
    show: Boolean,
    title: String? = null,
    message: String,
    icon: @Composable (() -> Unit)? = null,
    confirmText: String = stringResource(R.string.ok),
    cancelText: String = stringResource(R.string.cancel),
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    if (!show || message.isBlank()) return

    AlertDialog(
        onDismissRequest = onCancel,
        icon = icon,
        title = if (title.isNullOrBlank()) {
            null
        } else {
            { Text(text = title) }
        },
        text = { Text(text = message) },
        confirmButton = { AppTextButton(text = confirmText, onClick = onConfirm) },
        dismissButton = { AppTextButton(text = cancelText, onClick = onCancel) }
    )
}