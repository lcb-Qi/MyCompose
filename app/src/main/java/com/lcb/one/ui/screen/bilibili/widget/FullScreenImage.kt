package com.lcb.one.ui.screen.bilibili.widget

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import com.github.panpf.zoomimage.CoilZoomAsyncImage
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DimenUtils
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

class ImageViewState {
    private val defaultDuration = 250L
    private val defaultInterpolator = LinearInterpolator()
    private val screenWidth = AppUtils.getScreenWidth()
    private val screenHeight = AppUtils.getScreenHeight()

    var show by mutableStateOf(false)

    var offsetX = mutableIntStateOf(0)
    var offsetY = mutableIntStateOf(0)
    var width = mutableIntStateOf(0)
    var height = mutableIntStateOf(0)
    var alpha = mutableFloatStateOf(0f)

    fun doEnter(initOffset: IntOffset, initSize: IntSize) {
        show = true
        animateFloatState(alpha, 0f, 1f)
        animateIntState(offsetX, initOffset.x, 0)
        animateIntState(offsetY, initOffset.y, 0)
        animateIntState(width, initSize.width, screenWidth)
        animateIntState(height, initSize.height, screenHeight)
    }

    fun doExit(targetOffset: IntOffset, targetSize: IntSize) {
        animateFloatState(alpha, 1f, 0f)
        animateIntState(offsetX, 0, targetOffset.x)
        animateIntState(offsetY, 0, targetOffset.y)
        animateIntState(width, screenWidth, targetSize.width) { show = false }
        animateIntState(height, screenHeight, targetSize.height) { show = false }
    }

    private fun animateFloatState(
        state: MutableState<Float>, vararg values: Float, onEnd: () -> Unit = {}
    ) {
        val animator = ValueAnimator.ofFloat(*values).apply {
            interpolator = defaultInterpolator
            duration = defaultDuration
            addUpdateListener {
                state.value = it.animatedValue as Float
            }
            doOnEnd { onEnd() }
        }

        animator.start()
    }

    private fun animateIntState(
        state: MutableState<Int>, vararg values: Int, onEnd: () -> Unit = {}
    ) {
        val animator = ValueAnimator.ofInt(*values).apply {
            interpolator = defaultInterpolator
            duration = defaultDuration
            addUpdateListener {
                state.value = it.animatedValue as Int
            }
            doOnEnd { onEnd() }
        }

        animator.start()
    }
}


@Composable
fun FullScreenImage(
    modifier: Modifier = Modifier,
    state: ImageViewState,
    url: String,
    onClick: () -> Unit = {}
) {
    if (!state.show) return

    val scope = rememberCoroutineScope()
    val download: () -> Unit = {
        scope.launch {
            DownLoadUtil.saveImageFromUrl(url)
                .onSuccess { ToastUtils.showToast("保存成功 $it") }
                .onFailure { ToastUtils.showToast("保存失败") }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = state.alpha.floatValue))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilZoomAsyncImage(modifier = modifier
            .offset {
                IntOffset(x = state.offsetX.intValue, y = state.offsetY.intValue)
            }
            .size(
                width = DimenUtils.px2dp(state.width.intValue).dp,
                height = DimenUtils.px2dp(state.height.intValue).dp
            )
            .weight(1f),
            model = url,
            scrollBar = null,
            contentDescription = null,
            onTap = { onClick() })

        OutlinedButton(
            onClick = download,
            modifier = Modifier.padding(vertical = 32.dp),
        ) {
            Text(text = "保存")
        }
    }
}