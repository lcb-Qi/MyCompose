package com.lcb.one.ui

import android.app.Application
import com.lcb.one.util.android.LLog

class MyApp : Application() {
    companion object {
        private const val TAG = "MyApp"

        fun getAppContext(): Application {
            return app ?: throw RuntimeException("App is null!")
        }

        private var app: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        LLog.d(TAG, "Application onCreate...")
        registerActivityLifecycleCallbacks(LifecycleCallbackImpl)
    }
}
