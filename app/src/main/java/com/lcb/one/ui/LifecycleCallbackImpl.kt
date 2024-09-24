package com.lcb.one.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lcb.one.util.platform.LLogger


class LifecycleCallbackImpl : Application.ActivityLifecycleCallbacks {
    companion object {
        private const val TAG = "LifecycleCallback"
    }

    private fun logLifecycle(activity: Activity, lifecycleName: String) = LLogger.info(TAG) {
        val name = activity.javaClass.name
        val hashCode = activity.hashCode()
        "$lifecycleName: $name@$hashCode"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logLifecycle(activity, "onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        logLifecycle(activity, "onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        logLifecycle(activity, "onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        logLifecycle(activity, "onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        logLifecycle(activity, "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logLifecycle(activity, "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        logLifecycle(activity, "onActivityDestroyed")
    }
}