package com.lcb.one.util.android

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.lcb.one.BuildConfig
import com.lcb.one.ui.MyApp

data class AppVersionInfo(val versionCode: Int, val versionName: String)

const val PACKAGE_ME = BuildConfig.APPLICATION_ID

object AppUtils {
    fun immerse(
        view: View,
        @WindowInsetsCompat.Type.InsetsType type: Int = WindowInsetsCompat.Type.systemBars(),
        lightTheme: Boolean,
        @ColorInt color: Int = Color.TRANSPARENT
    ) {
        val activity = view.context as Activity
        val window = activity.window
        val controller = WindowCompat.getInsetsController(window, view)
        when (type) {
            // 沉浸入导航栏和状态栏
            WindowInsetsCompat.Type.systemBars() -> {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.statusBarColor = color
                window.navigationBarColor = color
                controller.isAppearanceLightStatusBars = lightTheme
                controller.isAppearanceLightNavigationBars = lightTheme
            }
            // 只沉浸入状态栏
            WindowInsetsCompat.Type.statusBars() -> {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.statusBarColor = color
                controller.isAppearanceLightStatusBars = lightTheme
            }
            // 只沉浸入导航栏
            WindowInsetsCompat.Type.navigationBars() -> {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.navigationBarColor = color
                controller.isAppearanceLightNavigationBars = lightTheme
            }

            else -> {}
        }
    }

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
}