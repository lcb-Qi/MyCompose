package com.lcb.one.util.platform

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Size
import android.view.WindowManager
import androidx.annotation.Px
import com.lcb.one.ui.MyApp

object PhoneUtil {
    private const val BRAND_XIAOMI = "Xiaomi"
    private const val BRAND_MEIZU = "meizu"
    private const val OS_KEY_XIAOMI = "ro.mi.os.version.incremental"
    private const val OS_KEY_MEIZU = "ro.flyme.version.id"

    fun getBrand(): String = Build.BRAND

    fun getOS(): String {
        val os = when (getBrand()) {
            BRAND_XIAOMI -> SystemPropUtils.getString(OS_KEY_XIAOMI)
            BRAND_MEIZU -> SystemPropUtils.getString(OS_KEY_MEIZU)
            else -> ""
        }

        return os
    }

    fun getModel(): String = Build.MODEL

    fun getSdkInt(): Int = Build.VERSION.SDK_INT

    fun getResolution(context: Context = MyApp.get()): Size {
        val metrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        return Size(metrics.bounds.width(), metrics.bounds.height())
    }

    @Px
    fun getScreenWidth(context: Context = MyApp.get()): Int = getResolution(context).width

    @Px
    fun getScreenHeight(context: Context = MyApp.get()): Int = getResolution(context).height

    fun getScreenFPS(context: Activity): Float {
        return context.display?.refreshRate ?: 0f
    }
}