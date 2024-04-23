package com.lcb.one.util.common

import android.os.Looper

object ThreadUtils {
    fun isOnMainThread(): Boolean = Looper.getMainLooper().thread === Thread.currentThread()
    fun isOnBackgroundThread(): Boolean = !isOnMainThread()

    fun assertOnMainThread() = require(isOnMainThread()) {
        "You must call this method on the main thread"
    }

    fun assertOnBackgroundThread() = require(isOnBackgroundThread()) {
        "You must call this method on a background thread"
    }
}