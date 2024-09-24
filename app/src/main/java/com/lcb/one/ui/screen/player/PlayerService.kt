package com.lcb.one.ui.screen.player

import android.content.Intent
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.lcb.one.util.platform.LLogger

@UnstableApi
class PlayerService : MediaSessionService() {
    companion object {
        private const val TAG = "PlayerService"
    }

    private val exoPlayer by lazy {
        val player = ExoPlayer.Builder(this).apply {
            val factory = DefaultRenderersFactory(applicationContext)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            setRenderersFactory(factory)
        }.build()

        player.apply {
            repeatMode = PlayerManager.instance.repeatMode
            shuffleModeEnabled = PlayerManager.instance.isShuffle
        }
    }

    private val mediaSession by lazy {
        MediaSession.Builder(this, exoPlayer).build()
    }


    override fun onCreate() {
        super.onCreate()
        LLogger.debug(TAG) { "onCreate: " }
        exoPlayer.prepare()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        LLogger.debug(TAG) { "onGetSession: " }
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        LLogger.debug(TAG) { "onTaskRemoved: stopSelf now" }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        LLogger.debug(TAG) { "onDestroy: " }
        exoPlayer.release()
        mediaSession.release()
    }
}