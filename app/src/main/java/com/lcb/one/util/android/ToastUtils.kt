package com.lcb.one.util.android

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.lcb.one.ui.MyApp
import com.lcb.one.util.common.ThreadUtils

object ToastUtils {
    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        if (msg.isBlank()) return

        if (ThreadUtils.isOnMainThread()) {
            Toast.makeText(MyApp.getAppContext(), msg, duration).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(MyApp.getAppContext(), msg, duration).show()
            }
        }
    }
}