package com.lcb.one.ui.screen.player.widget

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lcb.one.R
import com.lcb.one.ui.screen.player.PlayerHelper
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.weight.AppIconButton

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
            modifier = Modifier.scale(1.5f),
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
        playingMusic = playing,
        onDismiss = { showPlayList = false },
        onItemSelected = { onEvent(ControllerEvent.SeekTo(index = it)) }
    )
}

@Composable
fun SimplePlayerController(
    modifier: Modifier = Modifier,
    showPlay: Boolean,
    playingMusic: Music?,
    onControllerEvent: (event: ControllerEvent) -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var albumPic: Bitmap? by remember { mutableStateOf(null) }
        LaunchedEffect(playingMusic) {
            albumPic = PlayerHelper.getAlbumPicture(playingMusic?.uri)
        }

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(albumPic)
                .error(R.mipmap.ic_launcher)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.small),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .defaultMinSize(minHeight = 48.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                text = playingMusic?.title ?: "",
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1
            )
            Text(
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                text = playingMusic?.artistAndAlbum ?: "",
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }

        AppIconButton(
            icon = if (showPlay) {
                Icons.Rounded.PlayCircle
            } else {
                Icons.Rounded.PauseCircle
            },
            onClick = { onControllerEvent(ControllerEvent.PlayOrPause) }
        )
    }
}