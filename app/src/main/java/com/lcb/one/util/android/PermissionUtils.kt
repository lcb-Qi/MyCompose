package com.lcb.one.util.android

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.lcb.one.ui.MyApp

object PermissionUtils {
    private const val TAG = "PermissionUtils"

    fun requestPermission(activity: Activity, permission: String, requestCode: Int = 0) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        val showRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        LLog.d(TAG, "shouldShowRationale: $permission == $showRationale")

        return showRationale
    }

    fun hasAllFileAccess() = Environment.isExternalStorageManager()

    fun hasPermission(context: Context = MyApp.getAppContext(), permission: String): Boolean {
        val hasSelfPermission =
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        LLog.d(TAG, "hasPermission: $permission == $hasSelfPermission")

        return hasSelfPermission
    }
}