package com.lcb.one.util.android

import com.lcb.one.ui.MyApp

object DimenUtils {
    private val density = MyApp.get().resources.displayMetrics.density
    private val scaledDensity = MyApp.get().resources.displayMetrics.scaledDensity
    fun dp2px(dpValue: Int): Int {

        return (dpValue * density + 0.5f).toInt()
    }

    fun px2dp(pxValue: Int): Int {
        return (pxValue / density + 0.5f).toInt()
    }

    fun sp2px(spValue: Int): Float {
        return spValue * scaledDensity
    }
}