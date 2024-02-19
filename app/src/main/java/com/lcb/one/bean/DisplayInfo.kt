package com.lcb.one.bean

import android.content.Context
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lcb.one.ui.MyApp
import com.lcb.one.util.android.DimenUtils
import com.lcb.one.util.android.PhoneUtil

data class DisplayInfo(
    var displaySize: Size = Size(0, 0),
    var resolution: Size = Size(0, 0),
    var smallWidth: Int = 0,
    var density: Float = 0f,
    var dpi: Int = 0,
    var statusBarsHeight: Int = 0,
    var navigationBarsHeight: Int = 0
) {
    fun displaySize2dp(): String {
        val widthInDip = DimenUtils.px2dp(displaySize.width)
        val heightInDip = DimenUtils.px2dp(displaySize.height)
        return "${widthInDip}x${heightInDip}"
    }

    fun resolution2dp(): String {
        val widthInDip = DimenUtils.px2dp(resolution.width)
        val heightInDip = DimenUtils.px2dp(resolution.height)
        return "${widthInDip}x${heightInDip}"
    }

    companion object {
        fun obtain(context: Context = MyApp.getAppContext()): DisplayInfo {
            val displayInfo = DisplayInfo()

            val outMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(WindowManager::class.java)
            windowManager.defaultDisplay.getMetrics(outMetrics)

            displayInfo.apply {
                displaySize = Size(outMetrics.widthPixels, outMetrics.heightPixels)
                resolution = PhoneUtil.getResolution(context)
                density = outMetrics.density
                dpi = outMetrics.densityDpi
                val activity = context as? AppCompatActivity
                // statusBarsHeight = activity?.getStatusBarsHeight() ?: -1
                // navigationBarsHeight = activity?.getNavigationBarsHeight() ?: -1
                smallWidth = context.resources.configuration.smallestScreenWidthDp
            }

            return displayInfo
        }
    }
}