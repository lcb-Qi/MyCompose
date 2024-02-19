package com.lcb.one.ui.widget

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToolButton(title: String, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .defaultMinSize(80.dp, 45.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = title, color = Color.White, maxLines = 1)
    }
}
