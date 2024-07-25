package com.lcb.one.ui.screen.player.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.screen.player.repo.MusicPlayer
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppIconButton

@Composable
fun PlayListPage(
    playList: List<Music>,
    playingMusic: Music?,
    showPlay: Boolean,
) {
    Scaffold(topBar = { ToolBar(title = "Audio Player") }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PlayList(
                modifier = Modifier.weight(1f),
                playList = playList,
                playingAudio = playingMusic,
                onItemClick = {
                    MusicPlayer.handleCommand(ControllerEvent.SeekTo(it))
                }
            )

            Card(
                onClick = { MusicPlayer.showPlayingScreen() },
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                SimplePlayerController(
                    modifier = Modifier.padding(16.dp),
                    showPlay = showPlay,
                    playingAudio = playingMusic,
                    onControllerEvent = { MusicPlayer.handleCommand(it) },
                )
            }
        }
    }
}

@Composable
private fun PlayList(
    modifier: Modifier = Modifier,
    playList: List<Music>,
    playingAudio: Music?,
    onItemClick: (index: Int) -> Unit = {}
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(playList.size, key = { it }) { index ->
            val audioItem = playList[index]
            val textColor = if (playingAudio?.uri == audioItem.uri) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Unspecified
            }
            ListItem(
                modifier = Modifier.clickable { onItemClick(index) },
                headlineContent = {
                    Text(
                        text = audioItem.title,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                },
                supportingContent = {
                    Text(
                        text = audioItem.artist,
                        color = textColor
                    )
                },
                trailingContent = {
                    Text(
                        text = MusicPlayer.formatDuration(audioItem.duration),
                        color = textColor
                    )
                }
            )
        }
    }
}
