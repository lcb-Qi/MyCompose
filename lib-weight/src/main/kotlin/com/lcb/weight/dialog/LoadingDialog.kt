package com.lcb.weight.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingDialog(show: Boolean, message: String = "") {
    if (!show) return

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator()
                if (message.isNotBlank()) {
                    Text(text = message)
                }
            }
        }
    )
}