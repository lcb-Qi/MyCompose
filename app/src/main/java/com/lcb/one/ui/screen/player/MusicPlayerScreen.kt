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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.ANIMATE_DURATION
import com.lcb.one.ui.screen.player.widget.PlayDetailPage
import com.lcb.one.ui.screen.player.widget.PlayListPage
import com.lcb.one.ui.screen.privacy.PrivacyScreen
import com.lcb.weight.dialog.SimpleMessageDialog
import com.lcb.one.util.platform.AppUtils
import com.lcb.one.util.platform.Res
import kotlinx.coroutines.delay


@OptIn(UnstableApi::class)
object MusicPlayerScreen : Screen() {
    override val label: String = Res.string(R.string.music_player)

    private val playerManager = PlayerManager.instance

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        if (!AppUtils.hasPermission(permission = Manifest.permission.READ_MEDIA_AUDIO)) {
            var show by remember { mutableStateOf(true) }
            SimpleMessageDialog(
                show = show,
                title = stringResource(R.string.music_player),
                message = stringResource(R.string.msg_request_audio_permission),
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

        val playList by playerManager.playList.collectAsState()
        val playingMusic by playerManager.playingMusic.collectAsState()
        val showPlay by playerManager.showPlay.collectAsState()

        val listState = rememberLazyListState()

        AnimatedContent(
            targetState = playerManager.showPlayDetailPage,
            label = "showPlayDetailPage",
            transitionSpec = {
                if (playerManager.showPlayDetailPage) {
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
                        player = playerManager,
                        playList = playList,
                        playingMusic = playingMusic,
                        showPlay = showPlay
                    )
                } else {
                    PlayListPage(
                        playerManager = playerManager,
                        playList = playList,
                        playingMusic = playingMusic,
                        showPlay = showPlay,
                        listState = listState
                    )
                }
            }
        )

        LaunchedEffect(Unit) {
            // FIXME: 页面跳转动画还没结束，列表就开始渲染的话，会卡一下
            delay(ANIMATE_DURATION.toLong())
            playerManager.preparePlayer()
        }
    }
}
