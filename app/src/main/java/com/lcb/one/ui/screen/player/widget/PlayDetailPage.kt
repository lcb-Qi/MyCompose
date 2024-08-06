package com.lcb.one.ui.screen.player.widget

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.util.UnstableApi
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lcb.one.R
import com.lcb.one.ui.screen.player.PlayerHelper
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.screen.player.PlayerManager
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.util.android.PhoneUtil
import kotlinx.coroutines.delay

private val horizontalPadding = 32.dp

@Composable
private fun calculateImageSize(): DpSize = with(LocalDensity.current) {
    val screenWidthInDp = PhoneUtil.getScreenWidth().toDp()

    val size = screenWidthInDp - horizontalPadding * 2

    DpSize(size, size)
}

@OptIn(UnstableApi::class)
@Composable
fun PlayDetailPage(
    player: PlayerManager,
    playList: List<Music>,
    playingMusic: Music?,
    showPlay: Boolean,
) {
    BackHandler { player.showPlayListPage() }

    Scaffold(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                val (backIconRef, textRef) = createRefs()
                AppIconButton(
                    modifier = Modifier.constrainAs(backIconRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    icon = Icons.Rounded.KeyboardArrowDown,
                    onClick = { player.showPlayListPage() }
                )

                Text(
                    text = stringResource(R.string.song),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.constrainAs(textRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }

            val isPlaying by player.isPlaying.collectAsState()
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("player.json"))
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .size(calculateImageSize())
                    .scale(2.0f)
                    .padding(horizontal = horizontalPadding),
                iterations = LottieConstants.IterateForever,
                isPlaying = isPlaying,
                restartOnPlay = true
            )

            PlayingMusicInfo(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                playingAudio = playingMusic
            )

            val currentMax by remember(playingMusic) {
                derivedStateOf { playingMusic?.duration ?: 0 }
            }
            var currentPosition by remember { mutableLongStateOf(0L) }
            val activity = LocalContext.current as Activity
            LaunchedEffect(Unit) {
                // FIXME: 拖动 slider 时不松开，这里也会更新进度，导致slider跳动，sliderState 里有 drag 状态但是无法访问
                while (true) {
                    activity.runOnUiThread { currentPosition = player.getCurrentPosition() }
                    delay(1000)
                }
            }
            PlayerProgressIndicator(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                value = currentPosition.toFloat(),
                maxValue = currentMax.toFloat(),
                onValueChange = { currentPosition = it.toLong() },
                onValueChangeFinished = {
                    val event = ControllerEvent.SeekToPosition(currentPosition)
                    player.handleEvent(event)
                },
                nowPosition = currentPosition,
            )

            PlayerController(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                playList = playList,
                repeatMode = player.repeatMode,
                isShuffle = player.isShuffle,
                playing = playingMusic,
                showPlay = showPlay,
                onEvent = { player.handleEvent(it) }
            )
        }
    }
}

@Composable
private fun PlayingMusicInfo(modifier: Modifier = Modifier, playingAudio: Music?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 72.dp),
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                text = playingAudio?.title ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = playingAudio?.artistAndAlbum ?: "",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1
            )
        }
    )
}

@Composable
private fun PlayerProgressIndicator(
    modifier: Modifier = Modifier,
    value: Float = 0f,
    maxValue: Float = 0f,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
    nowPosition: Long,
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (sliderRef, nowPositionRef, durationRef) = createRefs()
        LegacySlider(
            modifier = Modifier.constrainAs(sliderRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            value = value,
            maxValue = maxValue,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished
        )

        Text(
            modifier = Modifier
                .constrainAs(nowPositionRef) {
                    start.linkTo(parent.start)
                    top.linkTo(sliderRef.bottom)
                }
                .padding(start = 8.dp),
            text = PlayerHelper.formatDuration(nowPosition),
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            modifier = Modifier
                .constrainAs(durationRef) {
                    end.linkTo(parent.end)
                    top.linkTo(sliderRef.bottom)
                }
                .padding(end = 8.dp),
            text = PlayerHelper.formatDuration(maxValue.toLong()),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LegacySlider(
    modifier: Modifier = Modifier,
    value: Float,
    maxValue: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val trackHeight = 4.dp
    val thumbSize = DpSize(16.dp, 16.dp)
    Slider(
        interactionSource = interactionSource,
        modifier = modifier.requiredSizeIn(minWidth = thumbSize.width, minHeight = trackHeight),
        value = value,
        valueRange = 0f..maxValue,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = interactionSource,
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
                sliderState = it,
                modifier = Modifier.height(trackHeight),
                thumbTrackGapSize = 0.dp,
                trackInsideCornerSize = 0.dp,
                drawStopIndicator = null
            )
        }
    )
}