package com.lcb.one.util.android

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.graphics.drawable.toBitmapOrNull
import com.lcb.one.BuildConfig
import com.lcb.one.ui.MyApp

class AppVersionInfo(val versionCode: Int, val versionName: String)

const val PACKAGE_ME = BuildConfig.APPLICATION_ID

object AppUtils {
    private const val TAG = "AppUtils"

    fun isNetworkAvailable(context: Context = MyApp.getAppContext()): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return if (networkCapabilities == null) {
            false
        } else {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    || networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
    }

    fun getAppVersionInfo(
        context: Context = MyApp.getAppContext(),
        packageName: String
    ): AppVersionInfo {
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(packageName, 0)

        return AppVersionInfo(packageInfo.versionCode, packageInfo.versionName)
    }

    fun getAppVersionName(context: Context = MyApp.getAppContext(), packageName: String): String {
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(packageName, 0)

        return packageInfo.versionName
    }

    fun getAppVersionCode(context: Context = MyApp.getAppContext(), packageName: String): Int {
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(packageName, 0)

        return packageInfo.versionCode
    }

    fun getNavigationBarsHeight(context: Context = MyApp.getAppContext()): Int {
        val metrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        return insets.bottom
    }

    fun getStatusBarsHeight(context: Context = MyApp.getAppContext()): Int {
        val metrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        return insets.top
    }

    fun installApk(context: Context = MyApp.getAppContext(), uri: Uri) {
        LLog.d(TAG, "installApk: uri = $uri")
        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "application/vnd.android.package-archive")
        }
        context.startActivity(installIntent)
    }

    fun getAllPackageName(context: Context = MyApp.getAppContext()): List<String> {
        val pm = context.packageManager
        val info = pm.getInstalledPackages(0)
        return info.map { it.packageName }
    }

    fun getAppIcon(context: Context = MyApp.getAppContext(), packageName: String): Bitmap? {
        val icon: Bitmap?
        val pm = context.packageManager

        when (val drawable = pm.getApplicationIcon(packageName)) {
            is BitmapDrawable -> icon = drawable.bitmap

            is AdaptiveIconDrawable -> {
                val backgroundDr = drawable.background
                val foregroundDr = drawable.foreground
                val drr = arrayOfNulls<Drawable>(2)
                drr[0] = backgroundDr
                drr[1] = foregroundDr
                val layerDrawable = LayerDrawable(drr)
                val width = layerDrawable.getIntrinsicWidth()
                val height = layerDrawable.getIntrinsicHeight()
                icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(icon)
                layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
                layerDrawable.draw(canvas)
            }

            else -> icon = drawable.toBitmapOrNull()
        }

        return icon
    }

    fun getApkPath(packageName: String): String {
        val pm = MyApp.getAppContext().packageManager
        val info = pm.getApplicationInfo(packageName, 0)

        return info.sourceDir
    }

    fun isSystemDarkTheme(): Boolean {
        val uiModeManager = MyApp.getAppContext().getSystemService(UiModeManager::class.java)
        return uiModeManager.nightMode == Configuration.UI_MODE_NIGHT_YES
    }
}