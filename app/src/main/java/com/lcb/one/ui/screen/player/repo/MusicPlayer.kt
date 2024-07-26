package com.lcb.one.ui.screen.player.repo

import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.player.MusicPlayerService
import com.lcb.one.ui.widget.settings.storage.disk.BooleanPrefState
import com.lcb.one.ui.widget.settings.storage.disk.IntPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.UserPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(UnstableApi::class)
class MusicPlayer {
    companion object {
        private const val TAG = "MusicPlayer"
        private const val EXT_MUSIC = "ext_music"
        val instance by lazy { MusicPlayer() }

        fun formatDuration(duration: Long): String {
            val minutes = duration / 1000 / 60
            val seconds = duration / 1000 % 60
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }


    var showPlayDetailPage by mutableStateOf(false)
        private set
    val playList: MutableStateFlow<List<Music>> = MutableStateFlow(emptyList())

    val isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun showPlayListPage() {
        showPlayDetailPage = false
    }

    fun showPlayDetailPage() {
        showPlayDetailPage = true
    }

    private lateinit var player: MediaController

    val playingMusic: MutableStateFlow<Music?> = MutableStateFlow(null)
    val showPlay: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            showPlay.update { Util.shouldShowPlayButton(player) }
            this@MusicPlayer.isPlaying.update { isPlaying }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            playingMusic.update {
                val current = mediaItem?.getExtraAudioItem()

                LLog.d(TAG, "onMediaItemTransition: $current")
                current?.let {
                    UserPref.putString(UserPref.Key.PLAYER_LAST_MUSIC, it.uri.toString())
                }

                current
            }
        }
    }

    private fun MediaItem.getExtraAudioItem(): Music? {
        val extras = mediaMetadata.extras ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras.getParcelable(EXT_MUSIC, Music::class.java)
        } else {
            extras.getParcelable(EXT_MUSIC) as? Music
        }
    }

    suspend fun preparePlayer(context: Context = MyApp.get()) {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        val runnable = Runnable {
            CoroutineScope(Dispatchers.Main.immediate).launch {
                player = controllerFuture.get()
                player.apply {
                    addListener(playerListener)

                    LLog.d(TAG, "preparePlayer: mediaItemCount = $mediaItemCount")
                    if (mediaItemCount == 0) {
                        loadMusics()
                    }

                    playingMusic.update { currentMediaItem?.getExtraAudioItem() }
                }
            }
        }

        controllerFuture.addListener(runnable, ContextCompat.getMainExecutor(context))
    }

    private suspend fun loadMusics() {
        playList.update { StorageUtils.findMusics() }
        val lastPlayMusicUri = getLastPlayMusicUri()
        var lastIndex = 0
        val mediaItems = playList.value.map { music ->
            if (music.uri.toString() == lastPlayMusicUri) {
                lastIndex = playList.value.indexOf(music)
            }
            MediaItem.Builder().apply {
                setUri(music.uri)
                setMediaId(music.uri.toString())

                val metadata = MediaMetadata.Builder()
                    .setExtras(bundleOf(EXT_MUSIC to music))
                    .build()
                setMediaMetadata(metadata)
            }.build()
        }
        player.setMediaItems(mediaItems)
        player.seekTo(lastIndex, 0)
        LLog.d(TAG, "loadMusics: total = ${mediaItems.size}, lastIndex = $lastIndex")
    }


    fun handleCommand(event: ControllerEvent) {
        LLog.d(TAG, "handleCommand: $event")
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

    private fun seekToNext() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
        }
    }

    private fun seekToPrevious() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        }
    }

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

    private fun getLastPlayMusicUri(): String {
        val uri = UserPref.getString(UserPref.Key.PLAYER_LAST_MUSIC, "")
        LLog.d(TAG, "getLastPlayMusic: uri = $uri")
        return uri
    }
}