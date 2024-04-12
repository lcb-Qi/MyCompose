package com.lcb.one.ui.widget.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Point(color: Color = MaterialTheme.colorScheme.primary, size: Dp = 8.dp) {
    Image(
        ColorPainter(color),
        "",
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(50))
    )
}