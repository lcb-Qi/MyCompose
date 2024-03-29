package com.lcb.one.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
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
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivityCreated: $name@$hashCode")
            }

            override fun onActivityStarted(activity: Activity) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivityStarted: $name@$hashCode")
            }

            override fun onActivityResumed(activity: Activity) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivityResumed: $name@$hashCode")
            }

            override fun onActivityPaused(activity: Activity) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivityPaused: $name@$hashCode")
            }

            override fun onActivityStopped(activity: Activity) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivityStopped: $name@$hashCode")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivitySaveInstanceState: $name@$hashCode")
            }

            override fun onActivityDestroyed(activity: Activity) {
                val name = activity.javaClass.name
                val hashCode = activity.hashCode()
                LLog.d(TAG, "onActivityDestroyed: $name@$hashCode")
            }
        })
    }
}