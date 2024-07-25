package com.lcb.one.ui.screen.player.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.widget.common.NoRippleInteractionSource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayListDialog(
    show: Boolean,
    playList: List<Music>,
    playingAudio: Music?,
    onDismiss: () -> Unit,
    onItemSelected: (index: Int) -> Unit
) {
    if (!show || playList.isEmpty()) return

    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp)) {
            item {
                Text(
                    text = "播放列表",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(count = playList.size, key = { it }) { index ->
                val audio = playList[index]
                val textColor = if (playingAudio?.uri == audio.uri) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Unspecified
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        interactionSource = NoRippleInteractionSource(),
                        indication = null
                    ) {
                        onItemSelected(index)
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = audio.title,
                            color = textColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = " - ${audio.artist}",
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (index != playList.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}