package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    day: Int,
    selected: Boolean,
    mcDay: Boolean,
    predictMcDay: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.small,
        selected = selected,
        onClick = onClick,
        border = if (selected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
        color = if (mcDay) {
            MaterialTheme.colorScheme.secondaryContainer
        } else if (predictMcDay) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Box(
            modifier = Modifier.requiredSize(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center,
            )
            if (predictMcDay) {
                Text(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    text = "预",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = MaterialTheme.colorScheme.error)
                )
            }
        }
    }
}