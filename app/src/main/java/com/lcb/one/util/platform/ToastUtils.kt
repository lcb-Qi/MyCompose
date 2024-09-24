package com.lcb.one.util.platform

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.lcb.one.ui.MyApp

object ToastUtils {
    fun send(message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (message.isBlank()) return

        val inUiThread = Thread.currentThread() === Looper.getMainLooper().thread

        if (inUiThread) {
            Toast.makeText(MyApp.get(), message, duration).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(MyApp.get(), message, duration).show()
            }
        }
    }
}