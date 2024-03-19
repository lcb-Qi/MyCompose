package com.lcb.one.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun TopAppBars(
    text: String,
    navigationIcon: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = navigationIcon,
        title = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .wrapContentSize()
                    .combinedClickable(
                        onLongClick = onLongClick,
                        indication = LocalIndication.current,
                        interactionSource = NoRippleInteractionSource(),
                    ) {
                        onClick?.invoke()
                    }
            )
        }
    )
}