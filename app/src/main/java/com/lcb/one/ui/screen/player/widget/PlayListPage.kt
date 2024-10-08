package com.lcb.one.ui.screen.player.widget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.screen.applist.widget.ToolButton
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.screen.player.PlayerManager
import com.lcb.weight.appbar.ToolBar
import com.lcb.weight.AppIconButton
import com.lcb.weight.noRippleClickable
import com.lcb.one.util.platform.ToastUtils
import com.lcb.one.util.platform.getRelativePath
import com.lcb.weight.AppTextButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

    var refreshing by remember { mutableStateOf(false) }
    var searchMode by remember { mutableStateOf(false) }
    val updatePlayList: () -> Unit = {
        scope.launch {
            refreshing = true
            delay(500)
            val newCount = playerManager.updatePlaylist()
            ToastUtils.send("新增 $newCount 首音乐")
            refreshing = false
        }
    }

    var keyword by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val isImeVisible = WindowInsets.isImeVisible
    BackHandler(searchMode && !isImeVisible) {
        searchMode = false
    }

    LaunchedEffect(searchMode) {
        if (searchMode) {
            delay(100)
            focusRequester.requestFocus()
        } else {
            keyword = ""
        }
    }

    Scaffold(
        topBar = {
            ToolBar(
                title = {
                    AnimatedContent(targetState = searchMode, label = "search") { isInSearch ->
                        if (isInSearch) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 8.dp)
                                    .focusRequester(focusRequester),
                                shape = MaterialTheme.shapes.extraLarge,
                                textStyle = MaterialTheme.typography.bodyLarge,
                                value = keyword,
                                placeholder = { Text(text = "搜索歌手或歌曲") },
                                onValueChange = { keyword = it },
                                maxLines = 1,
                                trailingIcon = {
                                    if (keyword.isNotEmpty()) {
                                        Icon(
                                            Icons.Rounded.Cancel,
                                            contentDescription = null,
                                            modifier = Modifier.noRippleClickable { keyword = "" }
                                        )
                                    }
                                },
                            )
                        } else {
                            Text(text = stringResource(R.string.music_player))
                        }
                    }
                },
                actions = {
                    val text = if (searchMode) stringResource(R.string.cancel) else "搜索"
                    AppTextButton(text = text, onClick = { searchMode = !searchMode })
                }
            )
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
        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = refreshing,
            onRefresh = updatePlayList
        ) {
            PlayListContent(
                listState, playList, playerManager,
                selectedIndex, showPlay, playingMusic,
                keyword
            )
        }
    }
}

@Composable
private fun PlayListContent(
    listState: LazyListState,
    playList: List<Music>,
    playerManager: PlayerManager,
    selectedIndex: Int,
    showPlay: Boolean,
    playingMusic: Music?,
    filter: String = ""
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(playList, { _, item -> item.uri }) { index, item ->
                val needShow = filter.isBlank()
                        || (item.title.contains(filter) || item.artist.contains(filter))
                if (needShow) {
                    var showMore by remember { mutableStateOf(false) }
                    PlayListItem(
                        modifier = Modifier.noRippleClickable {
                            playerManager.handleEvent(ControllerEvent.SeekTo(index))
                        },
                        selected = selectedIndex == index,
                        music = item,
                        onClickMore = { showMore = true }
                    )

                    if (index != playList.size - 1) {
                        HorizontalDivider()
                    }

                    MusicInfoDialog(
                        showMore,
                        item,
                        onSetNext = { playerManager.handleEvent(ControllerEvent.SetNext(index)) },
                        onDismiss = { showMore = false }
                    )
                }
            }
        }

        Card(
            onClick = { if (playingMusic != null) (playerManager.showPlayDetailPage()) },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            SimplePlayerController(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                showPlay = showPlay,
                playingMusic = playingMusic,
                onControllerEvent = { playerManager.handleEvent(it) },
            )
        }
    }
}

@Composable
private fun PlayListItem(
    modifier: Modifier = Modifier,
    music: Music,
    selected: Boolean,
    onClickMore: () -> Unit = {}
) {
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

        AppIconButton(icon = Icons.Rounded.MoreHoriz, onClick = onClickMore)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicInfoDialog(show: Boolean, music: Music, onSetNext: () -> Unit, onDismiss: () -> Unit) {
    if (!show) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            content = {
                ToolButton(text = music.title, style = MaterialTheme.typography.titleLarge)
                ToolButton(leadingIcon = Icons.Rounded.Person, text = music.artist)
                ToolButton(leadingIcon = Icons.Rounded.Album, text = music.album)
                ToolButton(leadingIcon = Icons.Rounded.WatchLater, text = "下一首播放") {
                    onDismiss()
                    onSetNext()
                }
                ToolButton(leadingIcon = Icons.Rounded.Folder, text = music.uri.getRelativePath())
            }
        )
    }
}
