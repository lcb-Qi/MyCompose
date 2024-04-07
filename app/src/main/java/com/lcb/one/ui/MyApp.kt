package com.lcb.one.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lcb.one.bean.McDay
import com.lcb.one.room.appDatabase
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils.toMillis
import com.lcb.one.util.common.ThreadPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

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

        CoroutineScope(Dispatchers.Main).launch {
            appDatabase.getMcDayDao().deleteAll()
            val list = listOf(
                McDay(
                    startTime = LocalDate.of(2024, 1, 1).toMillis(),
                    endTime = LocalDate.of(2024, 1, 5).toMillis()
                ),
                McDay(
                    startTime = LocalDate.of(2024, 1, 20).toMillis(),
                    endTime = LocalDate.of(2024, 1, 25).toMillis()
                ),
                McDay(
                    startTime = LocalDate.of(2024, 2, 15).toMillis(),
                    endTime = LocalDate.of(2024, 2, 28).toMillis()
                ),
                McDay(
                    startTime = LocalDate.of(2024, 3, 28).toMillis(),
                    endTime = LocalDate.of(2024, 4, 2).toMillis()
                ),
                McDay(
                    startTime = LocalDate.of(2024, 5, 5).toMillis(),
                    endTime = LocalDate.of(2024, 5, 20).toMillis()
                ),
                McDay(
                    startTime = LocalDate.of(2024, 5, 29).toMillis(),
                    endTime = LocalDate.of(2024, 6, 15).toMillis()
                ),
            )
            appDatabase.getMcDayDao().insert(*list.toTypedArray())
        }
    }
}
