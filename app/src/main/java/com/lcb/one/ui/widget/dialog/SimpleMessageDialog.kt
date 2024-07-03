package com.lcb.one.ui.widget.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lcb.one.localization.Localization
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun SimpleMessageDialog(
    show: Boolean,
    title: String? = null,
    message: String,
    icon: @Composable (() -> Unit)? = null,
    confirmText: String = Localization.ok,
    cancelText: String = Localization.cancel,
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