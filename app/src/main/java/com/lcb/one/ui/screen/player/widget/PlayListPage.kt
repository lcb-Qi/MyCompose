package com.lcb.one.ui.screen.player.widget

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.screen.player.PlayerHelper
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.screen.player.PlayerManager
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.ui.widget.common.noRippleClickable
import kotlinx.coroutines.launch

@Composable
fun PlayListPage(
    playerManager: PlayerManager,
    playList: List<Music>,
    playingMusic: Music?,
    showPlay: Boolean,
    listState: LazyListState = rememberLazyListState()
) {
    val scope = rememberCoroutineScope()

    val selectedIndex = remember(playList, playingMusic) {
        playList.indexOf(playingMusic).coerceAtLeast(0)
    }

    Scaffold(
        topBar = {
            ToolBar(title = stringResource(R.string.music_player), actions = {
                AppIconButton(icon = Icons.Rounded.Search, onClick = {
                    scope.launch { playerManager.updatePlaylist() }
                })
            })
        },
        floatingActionButton = {
            var showBack2Current by remember { mutableStateOf(false) }
            LaunchedEffect(listState, selectedIndex) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }.collect { visibleItem ->
                    showBack2Current = selectedIndex !in visibleItem.map { it.index }
                }
            }

            if (showBack2Current) {
                SmallFloatingActionButton(
                    modifier = Modifier.padding(bottom = 80.dp),
                    onClick = { scope.launch { listState.animateScrollToItem(selectedIndex) } },
                    content = { Icon(Icons.Rounded.MyLocation, null) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(playList.size, key = { it }) { index ->
                    PlayListItem(
                        modifier = Modifier.noRippleClickable {
                            playerManager.handleEvent(ControllerEvent.SeekTo(index))
                        },
                        selected = selectedIndex == index,
                        music = playList[index],
                    )

                    if (index != playList.size - 1) {
                        HorizontalDivider()
                    }
                }
            }

            Card(
                onClick = { playerManager.showPlayDetailPage() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                SimplePlayerController(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    showPlay = showPlay,
                    playingAudio = playingMusic,
                    onControllerEvent = { playerManager.handleEvent(it) },
                )
            }
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
        Surface(color = tint,
            shape = CircleShape,
            modifier = Modifier.size(height = 32.dp, width = 4.dp),
            content = {})

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
            text = PlayerHelper.formatDuration(music.duration),
            color = tint,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
