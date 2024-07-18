package com.lcb.one.util.android

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.lcb.one.ui.MyApp
import okio.buffer
import okio.sink

// Uri
fun Uri.outputStream() = MyApp.getContentResolver().openOutputStream(this)
fun Uri.inputStream() = MyApp.getContentResolver().openInputStream(this)

fun Uri.bufferedSink() = outputStream()?.sink()?.buffer()

fun Uri.getRelativePath(context: Context = MyApp.get()): String {
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

fun Uri.getDisplayName(context: Context = MyApp.get()): String {
    var displayName = ""
    val proj = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
    context.contentResolver.query(this, proj, null, null)?.use {
        if (it.moveToFirst()) {
            displayName = it.getStringOrNull(it.getColumnIndexOrThrow(proj[0])) ?: ""
        }
    }

    return displayName
}