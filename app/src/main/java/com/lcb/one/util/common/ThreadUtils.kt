package com.lcb.one.util.common

import android.os.Looper

object ThreadUtils {
    fun isOnMainThread(): Boolean = Looper.getMainLooper().thread === Thread.currentThread()
    fun isOnBackgroundThread(): Boolean = !isOnMainThread()

    @Throws(IllegalThreadStateException::class)
    fun forceOnMainThread() {
        if (!isOnMainThread()) {
            throw IllegalThreadStateException("Must be on main thread!")
        }
    }

    @Throws(IllegalThreadStateException::class)
    fun forceOnSubThread() {
        if (isOnMainThread()) {
            throw IllegalThreadStateException("Must be on sub thread!")
        }
    }
}