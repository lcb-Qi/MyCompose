package com.lcb.one.ui.screen.player.widget

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.lcb.one.R
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.widget.common.AppIconButton

private fun getRepeatModeIcon(repeatMode: Int, isShuffle: Boolean): Int {
    if (isShuffle) return R.drawable.ic_repeat_shuffle

    return when (repeatMode) {
        ExoPlayer.REPEAT_MODE_ALL -> R.drawable.ic_repeat_list
        ExoPlayer.REPEAT_MODE_ONE -> R.drawable.ic_repeat_one
        else -> R.drawable.ic_repeat_list
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    playList: List<Music>,
    repeatMode: Int,
    isShuffle: Boolean,
    showPlay: Boolean,
    playing: Music?,
    onEvent: (event: ControllerEvent) -> Unit
) {
    var showPlayList by remember { mutableStateOf(false) }

    val repeatModeIcon = remember(repeatMode, isShuffle) {
        getRepeatModeIcon(repeatMode, isShuffle)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppIconButton(
            painter = painterResource(repeatModeIcon),
            onClick = { onEvent(ControllerEvent.ChangeRepeatMode) }
        )

        AppIconButton(
            icon = Icons.Rounded.SkipPrevious,
            onClick = { onEvent(ControllerEvent.Previous) }
        )

        AppIconButton(
            modifier = Modifier.scale(2f),
            icon = if (showPlay) {
                Icons.Rounded.PlayCircle
            } else {
                Icons.Rounded.PauseCircle
            },
            onClick = { onEvent(ControllerEvent.PlayOrPause) },
        )

        AppIconButton(
            icon = Icons.Rounded.SkipNext,
            onClick = { onEvent(ControllerEvent.Next) }
        )

        AppIconButton(
            painter = painterResource(R.drawable.ic_play_list),
            onClick = { showPlayList = true }
        )
    }

    PlayListDialog(
        show = showPlayList,
        playList = playList,
        playingAudio = playing,
        onDismiss = { showPlayList = false },
        onItemSelected = { onEvent(ControllerEvent.SeekTo(index = it)) }
    )
}

@Composable
fun SimplePlayerController(
    modifier: Modifier = Modifier,
    showPlay: Boolean,
    playingAudio: Music?,
    onControllerEvent: (event: ControllerEvent) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp),
            painter = painterResource(R.mipmap.ic_launcher),
            contentDescription = null
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = playingAudio?.title ?: "",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = playingAudio?.artist ?: "",
                style = MaterialTheme.typography.labelMedium
            )
        }

        AppIconButton(
            icon = Icons.Rounded.SkipPrevious,
            onClick = { onControllerEvent(ControllerEvent.Previous) }
        )

        AppIconButton(
            icon = if (showPlay) {
                Icons.Rounded.PlayCircle
            } else {
                Icons.Rounded.PauseCircle
            },
            onClick = { onControllerEvent(ControllerEvent.PlayOrPause) }
        )

        AppIconButton(
            icon = Icons.Rounded.SkipNext,
            onClick = { onControllerEvent(ControllerEvent.Next) }
        )
    }
}