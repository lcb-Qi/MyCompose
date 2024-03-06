package com.lcb.one.ui.widget

import android.app.Activity
import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lcb.one.util.android.ToastUtils

private const val TIP_DURATION = 2000L

@Composable
fun FriendlyExitHandler(enable: Boolean = true) {
    val context = LocalContext.current
    var lastClickMillis: Long = 0
    BackHandler(enable) {
        val nowMillis = SystemClock.elapsedRealtime()
        lastClickMillis = if (nowMillis - lastClickMillis < TIP_DURATION) {
            (context as? Activity)?.finishAffinity()
            0
        } else {
            ToastUtils.showToast("再按一次返回键退出")
            nowMillis
        }
    }
}