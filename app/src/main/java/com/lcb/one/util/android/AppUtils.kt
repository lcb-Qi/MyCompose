package com.lcb.one.util.android

import android.app.Activity
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
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.Px
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.view.WindowInsetsControllerCompat
import com.lcb.one.BuildConfig
import com.lcb.one.ui.MyApp

const val PACKAGE_ME = BuildConfig.APPLICATION_ID

object AppUtils {
    fun isNetworkAvailable(context: Context = MyApp.get()): Boolean {
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

    @Px
    fun getNavigationBarsHeight(context: Context = MyApp.get()): Int {
        val metrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        return insets.bottom
    }

    @Px
    fun getStatusBarsHeight(context: Context = MyApp.get()): Int {
        val metrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        return insets.top
    }

    fun getAppIcon(context: Context = MyApp.get(), packageName: String): Bitmap? {
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
        val pm = MyApp.get().packageManager
        val info = pm.getApplicationInfo(packageName, 0)

        return info.sourceDir
    }

    fun launchSystemBrowser(context: Context = MyApp.get(), uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(uri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

    fun getAppDetailSettingsIntent(appId: String = PACKAGE_ME): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", appId, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun getAllFileAccessIntent(appId: String = PACKAGE_ME): Intent {
        return Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setData(Uri.fromParts("package", appId, null))
    }

    fun isSystemInDarkTheme(context: Context = MyApp.get()): Boolean {
        val uiMode = context.getSystemService(UiModeManager::class.java).nightMode
        return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    fun lightStatusBars(activity: Activity, light: Boolean) {
        val window = activity.window

        WindowInsetsControllerCompat(window, window.decorView).run {
            isAppearanceLightStatusBars = light
        }
    }
}