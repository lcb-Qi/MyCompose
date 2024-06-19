package com.lcb.one.ui.screen.main.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lcb.one.ui.widget.common.NoRippleInteractionSource

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun PoemTitle(poem: String, onClick: () -> Unit = {}, onLongClick: () -> Unit = {}) {
    Text(
        text = poem,
        modifier = Modifier
            .combinedClickable(
                onLongClick = onLongClick,
                indication = null,
                interactionSource = NoRippleInteractionSource(),
                onClick = onClick
            ),
        style = MaterialTheme.typography.titleMedium
    )
}