package com.lcb.one.ui.widget.common

import androidx.annotation.IntRange
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegacySlider(
    modifier: Modifier = Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val trackHeight = 4.dp
    val thumbSize = DpSize(16.dp, 16.dp)
    Slider(
        colors = colors,
        steps = steps,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier.requiredSizeIn(minWidth = thumbSize.width, minHeight = trackHeight),
        value = value,
        valueRange = valueRange,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = interactionSource,
                colors = colors,
                modifier = Modifier
                    .size(thumbSize)
                    .shadow(1.dp, CircleShape, clip = false)
                    .indication(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false, radius = 16.dp)
                    )
            )
        },
        track = {
            SliderDefaults.Track(
                colors = colors,
                sliderState = it,
                modifier = Modifier.height(trackHeight),
                thumbTrackGapSize = 0.dp,
                trackInsideCornerSize = 0.dp,
                drawStopIndicator = null
            )
        }
    )
}