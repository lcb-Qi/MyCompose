package com.lcb.one.ui.screen.player

import android.Manifest
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.util.UnstableApi
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Screen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.player.repo.MusicPlayer
import com.lcb.one.ui.screen.player.widget.PlayListPage
import com.lcb.one.ui.screen.player.widget.PlayDetailPage
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.PermissionUtils


@OptIn(UnstableApi::class)
object MusicPlayerScreen : Screen {
    override val route: String
        get() = "MusicPlayer"

    private val musicPlayer = MusicPlayer.instance

    @Composable
    override fun Content(args: Bundle?) {
        val navController = LocalNav.current!!
        if (!PermissionUtils.hasPermission(permission = Manifest.permission.READ_MEDIA_AUDIO)) {
            var show by remember { mutableStateOf(true) }
            SimpleMessageDialog(
                show = show,
                title = "Music Player",
                message = "请开启“读取音乐和音频”权限",
                onCancel = {
                    show = false
                    navController.popBackStack()
                },
                onConfirm = {
                    show = false
                    navController.launchSingleTop(PrivacyScreen)
                }
            )
            return
        }

        val playList by musicPlayer.playList.collectAsState()
        val playingMusic by musicPlayer.playingMusic.collectAsState()
        val showPlay by musicPlayer.showPlay.collectAsState()

        LaunchedEffect(Unit) { musicPlayer.preparePlayer() }

        AnimatedContent(
            targetState = musicPlayer.showPlayDetailPage,
            label = "showPlayDetailPage",
            transitionSpec = {
                if (musicPlayer.showPlayDetailPage) {
                    val enter = slideInVertically { it } + fadeIn()
                    val exit = slideOutVertically { -it } + fadeOut()
                    enter.togetherWith(exit)
                } else {
                    val enter = slideInVertically { -it } + fadeIn()
                    val exit = slideOutVertically { it } + fadeOut()
                    enter.togetherWith(exit)
                }
            },
            content = { showPlayDetailPage ->
                if (showPlayDetailPage) {
                    PlayDetailPage(
                        player = musicPlayer,
                        playList = playList,
                        playingMusic = playingMusic,
                        showPlay = showPlay
                    )
                } else {
                    PlayListPage(
                        player = musicPlayer,
                        playList = playList,
                        playingMusic = playingMusic,
                        showPlay = showPlay
                    )
                }
            }
        )
    }
}
