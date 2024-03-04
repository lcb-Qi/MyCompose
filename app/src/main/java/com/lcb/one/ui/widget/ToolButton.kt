package com.lcb.one.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToolButton(modifier: Modifier = Modifier, text: String, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = text, maxLines = 1)
    }
}
