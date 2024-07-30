package com.lcb.one.ui.screen.player.widget

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.screen.player.repo.MusicPlayer
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.noRippleClickable

@Composable
fun PlayListPage(
    player: MusicPlayer,
    playList: List<Music>,
    playingMusic: Music?,
    showPlay: Boolean,
) {
    Scaffold(topBar = { ToolBar(title = stringResource(R.string.music_player)) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PlayList(
                modifier = Modifier.weight(1f),
                playList = playList,
                playingAudio = playingMusic,
                onItemClick = { player.handleCommand(ControllerEvent.SeekTo(it)) }
            )

            Card(
                onClick = { player.showPlayDetailPage() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                SimplePlayerController(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    showPlay = showPlay,
                    playingAudio = playingMusic,
                    onControllerEvent = { player.handleCommand(it) },
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
            val music = playList[index]
            val selected = playingAudio?.uri == music.uri
            PlayListItem(
                modifier = Modifier.noRippleClickable { onItemClick(index) },
                selected = selected,
                music = music,
            )
        }
    }
}

@Composable
private fun PlayListItem(modifier: Modifier = Modifier, music: Music, selected: Boolean) {
    val tint = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Unspecified
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            color = tint,
            shape = CircleShape,
            modifier = Modifier.size(height = 32.dp, width = 4.dp),
            content = {}
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = music.title,
                color = tint,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = music.artistAndAlbum,
                color = tint,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = MusicPlayer.formatDuration(music.duration),
            color = tint,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
