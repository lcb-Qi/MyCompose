package com.lcb.one.ui.screen.player

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.media3.common.util.UnstableApi
import com.lcb.one.ui.Screen
import com.lcb.one.ui.screen.player.repo.MusicPlayer
import com.lcb.one.ui.screen.player.widget.PlayListPage
import com.lcb.one.ui.screen.player.widget.PlayDetailPage


@OptIn(UnstableApi::class)
object MusicPlayerScreen : Screen {
    override val route: String
        get() = "MusicPlayer"

    @Composable
    override fun Content(args: Bundle?) {
        val playList by MusicPlayer.playList.collectAsState()
        val playingMusic by MusicPlayer.playingMusic.collectAsState()
        val showPlay by MusicPlayer.showPlay.collectAsState()

        LaunchedEffect(Unit) { MusicPlayer.preparePlayer() }

        AnimatedContent(targetState = MusicPlayer.showPlayingScreen, label = "showPlayingScreen") {
            if (it) {
                PlayDetailPage(
                    playList = playList,
                    playingMusic = playingMusic,
                    showPlay = showPlay
                )
            } else {
                PlayListPage(
                    playList = playList,
                    playingMusic = playingMusic,
                    showPlay = showPlay
                )
            }
        }
    }
}
