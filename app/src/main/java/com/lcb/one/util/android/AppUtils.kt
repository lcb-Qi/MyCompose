package com.lcb.one.util.android

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.lcb.one.BuildConfig
import com.lcb.one.ui.MyApp

data class AppVersionInfo(val versionCode: Int, val versionName: String)

const val PACKAGE_ME = BuildConfig.APPLICATION_ID

object AppUtils {
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

    fun getAppVersionInfo(context: Context = MyApp.getAppContext(), packageName: String): AppVersionInfo {
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
}