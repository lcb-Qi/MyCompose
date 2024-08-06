package com.lcb.one.ui.screen.player

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.player.PlayerHelper.getExtraMusic
import com.lcb.one.ui.screen.player.PlayerHelper.toMediaItem
import com.lcb.one.ui.screen.player.repo.ControllerEvent
import com.lcb.one.ui.screen.player.repo.Music
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.LLogger
import com.lcb.one.util.android.UserPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class PlayerManager {
    companion object {
        private const val TAG = "PlayerManager"
        val instance by lazy { PlayerManager() }
    }

    private lateinit var player: MediaController

    val playList: MutableStateFlow<List<Music>> = MutableStateFlow(emptyList())
    val isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val playingMusic: MutableStateFlow<Music?> = MutableStateFlow(null)
    val showPlay: MutableStateFlow<Boolean> = MutableStateFlow(true)

    var showPlayDetailPage by mutableStateOf(false)
        private set

    fun showPlayListPage() {
        showPlayDetailPage = false
    }

    fun showPlayDetailPage() {
        showPlayDetailPage = true
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            showPlay.update { Util.shouldShowPlayButton(player) }
            this@PlayerManager.isPlaying.update { isPlaying }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            LLogger.debug(TAG) { "onMediaItemTransition: " }
            val current = mediaItem?.getExtraMusic() ?: return
            UserPref.putString(UserPref.Key.PLAYER_LAST_MUSIC, current.uri.toString())
            playingMusic.update { current }
        }
    }

    suspend fun preparePlayer(context: Context = MyApp.get()) {
        if (::player.isInitialized) {
            LLogger.debug(TAG) { "preparePlayer: has initialized, break" }
            return
        }
        LLogger.debug(TAG) { "preparePlayer: " }

        val token = SessionToken(context, ComponentName(context, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, token).buildAsync()

        val runnable = Runnable {
            CoroutineScope(Dispatchers.Main.immediate).launch {
                player = controllerFuture.get()
                player.apply {
                    addListener(playerListener)
                    // 先获取上一次播放的歌曲，再初始化播放列表，然后长时定位到上一次播放位置
                    val lastUri = PlayerHelper.getLastPlayMusicUri()
                    updatePlaylist()
                    val lastIndex = playList.value.indexOfFirst { it.uri.toString() == lastUri }
                    if (lastIndex in playList.value.indices) {
                        seekTo(lastIndex, 0)
                    }
                }
            }
        }

        controllerFuture.addListener(runnable, ContextCompat.getMainExecutor(context))
    }

    suspend fun updatePlaylist() {
        LLogger.debug(TAG) { "updatePlaylist: " }
        playList.update { PlayerHelper.findMusics() }
        val mediaItems = playList.value.toMediaItem()
        player.addMediaItems(mediaItems)
    }

    fun handleEvent(event: ControllerEvent) {
        LLogger.debug(TAG) { "handleEvent: $event" }
        when (event) {
            ControllerEvent.ChangeRepeatMode -> changedRepeatMode()
            ControllerEvent.PlayOrPause -> playOrPause()
            ControllerEvent.Next -> seekToNext()
            ControllerEvent.Previous -> seekToPrevious()
            is ControllerEvent.SeekTo -> seekTo(event.index, event.position)
            is ControllerEvent.SeekToPosition -> seekToPosition(event.position)
        }
    }

    fun getCurrentPosition() = player.currentPosition
    private fun playOrPause() = Util.handlePlayPauseButtonAction(player)
    private fun seekTo(index: Int, position: Long = 0) = player.seekTo(index, position)
    private fun seekToNext() = player.seekToNextMediaItem()
    private fun seekToPrevious() = player.seekToPreviousMediaItem()
    private fun seekToPosition(position: Long) = player.seekTo(position)

    var repeatMode by IntPrefState(UserPref.Key.PLAYER_REPEAT_MODE, ExoPlayer.REPEAT_MODE_ALL)
    var isShuffle by BooleanPrefState(UserPref.Key.PLAYER_IS_SHUFFLE, false)
    private fun changedRepeatMode() {
        if (player.shuffleModeEnabled) {
            player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            player.shuffleModeEnabled = false
        } else {
            when (player.repeatMode) {
                ExoPlayer.REPEAT_MODE_ALL -> {
                    player.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    player.shuffleModeEnabled = false
                }

                ExoPlayer.REPEAT_MODE_ONE -> {
                    player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
                    player.shuffleModeEnabled = true
                }

                else -> {
                    player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
                    player.shuffleModeEnabled = false
                }
            }
        }

        repeatMode = player.repeatMode
        isShuffle = player.shuffleModeEnabled
    }
}