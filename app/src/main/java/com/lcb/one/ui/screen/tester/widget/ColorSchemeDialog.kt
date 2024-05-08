package com.lcb.one.ui.screen.tester.widget

import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun ColorSchemeDialog(show: Boolean, onConfirm: () -> Unit) {
    if (!show) return

    AlertDialog(
        onDismissRequest = {},
        confirmButton = { AppTextButton(text = "确定", onClick = onConfirm) },
        text = {
            ColorSchemeList(
                modifier = Modifier.height(300.dp),
                colorScheme = MaterialTheme.colorScheme
            )
        }
    )
}