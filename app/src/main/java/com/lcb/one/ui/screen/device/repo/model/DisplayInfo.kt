package com.lcb.one.ui.screen.device.repo.model

import android.content.Context
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import com.lcb.one.ui.MyApp
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.DimenUtils
import com.lcb.one.util.android.PhoneUtil

data class DisplayInfo(
    var displaySize: Size = Size(-1, -1),
    var resolution: Size = Size(-1, -1),
    var smallWidth: Int = -1,
    var density: Float = -1f,
    var dpi: Int = -1,
    var statusBarsHeight: Int = -1,
    var navigationBarsHeight: Int = -1
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
                resolution = PhoneUtil.getResolution(context)
                density = outMetrics.density
                dpi = outMetrics.densityDpi
                statusBarsHeight = AppUtils.getStatusBarsHeight()
                navigationBarsHeight = AppUtils.getNavigationBarsHeight()
                smallWidth = context.resources.configuration.smallestScreenWidthDp
                displaySize = Size(
                    resolution.width,
                    resolution.height - statusBarsHeight - navigationBarsHeight
                )
            }

            return displayInfo
        }
    }
}

fun DisplayInfo.toMap(): Map<String, String> {
    return mapOf(
        "Resolution" to "$resolution px",
        "Display Size" to "$displaySize px",
        "Small Width" to "$smallWidth dp",
        "DPI" to dpi.toString(),
        "Density" to density.toString(),
        "StatusBars Height" to "$statusBarsHeight px",
        "NavigationBars Height" to "$navigationBarsHeight px",
    )
}