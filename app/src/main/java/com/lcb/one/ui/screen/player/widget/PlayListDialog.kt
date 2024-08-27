package com.lcb.one.ui.screen.player.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.widget.common.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayListDialog(
    show: Boolean,
    playList: List<Music>,
    playingMusic: Music?,
    onDismiss: () -> Unit,
    onItemSelected: (index: Int) -> Unit
) {
    if (!show || playList.isEmpty()) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 360.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                Text(
                    text = stringResource(R.string.play_list),
                    style = MaterialTheme.typography.titleLarge,
                )

                val listState =
                    rememberLazyListState(playList.indexOf(playingMusic), 0)
                LazyColumn(state = listState) {

                    items(count = playList.size, key = { it }) { index ->
                        val audio = playList[index]
                        val textColor = if (playingMusic?.uri == audio.uri) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Unspecified
                        }

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable { onItemSelected(index) }
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
        )
    }
}