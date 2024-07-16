package com.lcb.one.ui

import android.app.Application
import android.content.ContentResolver
import com.lcb.one.util.android.LLog

class MyApp : Application() {
    companion object {
        private const val TAG = "MyApp"

        fun get(): Application = app ?: throw RuntimeException("Application is null!")

        fun getContentResolver(): ContentResolver = get().contentResolver

        private var app: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        LLog.d(TAG, "Application onCreate...")
        registerActivityLifecycleCallbacks(LifecycleCallbackImpl())
    }
}
