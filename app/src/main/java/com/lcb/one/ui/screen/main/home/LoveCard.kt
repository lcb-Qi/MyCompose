package com.lcb.one.ui.screen.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.util.android.Res
import java.time.Duration
import java.time.LocalDateTime

private val startTime = LocalDateTime.of(2020, 12, 23, 0, 0, 0)

@Composable
fun LoveCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), onClick = {}) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp),
                painter = painterResource(R.drawable.icon_love),
                contentDescription = null
            )

            val title = remember { Res.string(R.string.fall_in_love) }
            Text(text = title, fontWeight = FontWeight.Medium)

            val duration = remember {
                val days = Duration.between(startTime, LocalDateTime.now()).toDays()

                "$days ${Res.string(R.string.days)}"
            }
            Text(
                text = duration,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}