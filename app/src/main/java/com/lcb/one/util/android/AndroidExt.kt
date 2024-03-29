package com.lcb.one.util.android

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import androidx.navigation.NavController
import androidx.navigation.navOptions

// Uri
fun Uri.toAbsolutePath(context: Context): String {
    var path = ""
    when (scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            context.contentResolver.query(this, proj, null, null)?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(proj[0])
                    path = it.getStringOrNull(columnIndex) ?: ""
                }

                if (path.isEmpty()) {
                    path = this.toFilePathForNonMediaUri(context)
                }
            }
        }

        // ContentResolver.SCHEME_ANDROID_RESOURCE -> {}
        ContentResolver.SCHEME_FILE -> path = getPath() ?: ""
        else -> path = getPath() ?: ""

    }

    return path
}

private fun Uri.toFilePathForNonMediaUri(context: Context): String {
    var path = ""
    context.contentResolver.query(this, null, null, null)?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow("_data")
            path = it.getStringOrNull(columnIndex) ?: ""
        }
    }

    return path
}

fun NavController.navigateSingleTop(route: String) {
    navigate(route = route, navOptions = navOptions { launchSingleTop = true })
}