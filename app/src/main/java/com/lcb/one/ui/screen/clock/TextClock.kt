package com.lcb.one.ui.screen.clock

import android.graphics.Typeface
import android.view.Gravity
import android.widget.TextClock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TextClock(
    modifier: Modifier = Modifier,
    builder: ((TextClock) -> Unit)? = null,
    update: ((TextClock) -> Unit)? = null
) {
    AndroidView(
        modifier = modifier,
        factory = {
            TextClock(it).apply {
                setTypeface(Typeface.MONOSPACE, Typeface.BOLD)
                gravity = Gravity.CENTER
                builder?.invoke(this)
            }
        },
        update = { update?.invoke(it) }
    )
}