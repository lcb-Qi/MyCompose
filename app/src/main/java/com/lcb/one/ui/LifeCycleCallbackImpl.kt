package com.lcb.one.ui

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.lcb.one.util.android.LLog

object LifeCycleCallbackImpl : ActivityLifecycleCallbacks {
    private const val TAG = "LifeCycleCallback"
    private var activeActivities = 0
    val isInBackground: Boolean = activeActivities == 0
    val isInForeground: Boolean = !isInBackground


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val name = activity.javaClass.name
        val hashCode = activity.hashCode()
        LLog.d(TAG, "onActivityCreated: $name@$hashCode")
    }

    override fun onActivityStarted(activity: Activity) {
        val name = activity.javaClass.name
        val hashCode = activity.hashCode()
        LLog.d(TAG, "onActivityStarted: $name@$hashCode")

        activeActivities++
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

        activeActivities--
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
}