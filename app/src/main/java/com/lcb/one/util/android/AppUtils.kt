package com.lcb.one.util.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.WindowInsets
import android.view.WindowManager
import com.lcb.one.BuildConfig
import com.lcb.one.ui.MyApp
import java.text.Collator
import java.util.Locale

data class AppVersionInfo(val versionCode: Int, val versionName: String)

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

    @SuppressLint("QueryPermissionsNeeded")
    fun getAllApps(context: Context): List<String> {
        val packageManager = context.packageManager
        val info = packageManager.getInstalledPackages(0).asSequence()
        return info.sortedWith { o1, o2 ->
            collator.compare(
                packageManager.getApplicationLabel(o1.applicationInfo),
                packageManager.getApplicationLabel(o2.applicationInfo)
            )
        }
            .map { it.packageName }
            .toList()
    }

    fun getUserApps(context: Context): List<String> {
        val packageManager = context.packageManager
        val info = packageManager.getInstalledPackages(0).asSequence()
        return info.filter { it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
            .sortedWith { o1, o2 ->
                collator.compare(
                    packageManager.getApplicationLabel(o1.applicationInfo),
                    packageManager.getApplicationLabel(o2.applicationInfo)
                )
            }
            .map { it.packageName }
            .toList()
    }

    fun getSystemApps(context: Context): List<String> {
        val packageManager = context.packageManager
        val info = packageManager.getInstalledPackages(0).asSequence()
        return info.filter { it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 }
            .sortedWith { o1, o2 ->
                collator.compare(
                    packageManager.getApplicationLabel(o1.applicationInfo),
                    packageManager.getApplicationLabel(o2.applicationInfo)
                )
            }
            .map { it.packageName }
            .toList()
    }

    val collator by lazy {
        Collator.getInstance(Locale.getDefault()).apply {
            strength = Collator.PRIMARY // 设置排序强度为主要级别，只比较基本的字符差异
            // decomposition = Collator.FULL_DECOMPOSITION // 设置字符分解为完全分解，以便正确处理包含变音符号的字符
        }
    }
}