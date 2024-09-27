package com.lcb.one.ui.screen.player.widget

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.lcb.one.R
import com.lcb.one.ui.screen.player.PlayerHelper
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.screen.player.PlayerManager
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.theme.AppThemeSurface
import com.lcb.weight.AppIconButton
import com.lcb.weight.LegacySlider
import com.lcb.one.util.platform.PhoneUtil
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
    AppThemeSurface(darkTheme = false) {
        BackHandler { player.showPlayListPage() }


        Box(modifier = Modifier.fillMaxSize()) {
            val activity = LocalContext.current as Activity
            val albumPic: MutableState<Any?> = remember { mutableStateOf(null) }
            LaunchedEffect(playingMusic) {
                if (playingMusic != null) {
                    albumPic.value = ImageRequest.Builder(activity)
                        .data(PlayerHelper.getAlbumPicture(playingMusic.uri))
                        .error(R.mipmap.ic_launcher)
                        .size(Size.ORIGINAL)
                        .crossfade(200)
                        .build()
                }
            }

            // 背景
            AsyncImage(
                model = albumPic.value,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f)
                    .blur(100.dp, BlurredEdgeTreatment(RoundedCornerShape(5.dp))),
                contentScale = ContentScale.Crop
            )

            // Content
            Scaffold(
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
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

                    AsyncImage(
                        model = albumPic.value,
                        contentDescription = null,
                        modifier = Modifier
                            .size(calculateImageSize())
                            .clip(MaterialTheme.shapes.medium)
                    )

                    PlayingMusicInfo(
                        modifier = Modifier.padding(horizontal = horizontalPadding),
                        playingMusic = playingMusic
                    )

                    val currentMax by remember(playingMusic) {
                        derivedStateOf { playingMusic?.duration ?: 0 }
                    }
                    var currentPosition by remember { mutableLongStateOf(0L) }
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
    }
}

@Composable
private fun PlayingMusicInfo(modifier: Modifier = Modifier, playingMusic: Music?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 72.dp),
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                text = playingMusic?.title ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = playingMusic?.artistAndAlbum ?: "",
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
            valueRange = 0f..maxValue,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            colors = SliderDefaults.colors().copy(
                thumbColor = Color.Black,
                activeTrackColor = Color.Black,
                inactiveTrackColor = Color.Gray.copy(alpha = 0.25f)
            )
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