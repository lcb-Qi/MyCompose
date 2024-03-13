package com.lcb.one.ui.widget

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ToolButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onclick: () -> Unit
) {
    Button(enabled = enabled, onClick = onclick, modifier = modifier) {
        Text(text = text, maxLines = 1)
    }
}
