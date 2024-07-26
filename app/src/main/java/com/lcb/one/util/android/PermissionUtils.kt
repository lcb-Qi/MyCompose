package com.lcb.one.util.android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.lcb.one.ui.MyApp

object PermissionUtils {
    fun requestPermission(activity: Activity, permission: String, requestCode: Int = 0) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        val showRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

        return showRationale
    }

    fun hasPermission(context: Context = MyApp.get(), permission: String): Boolean {
        val hasSelfPermission =
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

        return hasSelfPermission
    }

    fun canReadAudio(context: Context = MyApp.get()) =
        hasPermission(context, Manifest.permission.READ_MEDIA_AUDIO)

    fun canReadImage(context: Context = MyApp.get()) =
        hasPermission(context, Manifest.permission.READ_MEDIA_IMAGES)

    fun canAccessAllFile() = Environment.isExternalStorageManager()
}