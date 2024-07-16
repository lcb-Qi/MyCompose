package com.lcb.one.util.android

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.lcb.one.ui.MyApp

// Uri
fun Uri.toRelativePath(context: Context = MyApp.get()): String {
    var path = ""
    val proj = arrayOf(MediaStore.Images.Media.RELATIVE_PATH, MediaStore.Images.Media.DISPLAY_NAME)
    context.contentResolver.query(this, proj, null, null)?.use {
        if (it.moveToFirst()) {
            val relativePath = it.getStringOrNull(it.getColumnIndexOrThrow(proj[0])) ?: ""
            val displayName = it.getStringOrNull(it.getColumnIndexOrThrow(proj[1])) ?: ""

            path = relativePath + displayName
        }
    }

    return path
}