package com.lcb.one.util.platform

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.lcb.one.ui.MyApp
import okio.buffer
import okio.sink
import okio.source

// Uri
fun Uri.outputStream() = MyApp.getContentResolver().openOutputStream(this)
fun Uri.inputStream() = MyApp.getContentResolver().openInputStream(this)

fun Uri.bufferedSink() = outputStream()?.sink()?.buffer()
fun Uri.bufferedSource() = inputStream()?.source()?.buffer()

fun Uri.getAbsolutePath(context: Context = MyApp.get()): String {
    val filePath = when (scheme) {
        null -> path
        ContentResolver.SCHEME_FILE -> path
        ContentResolver.SCHEME_CONTENT -> {
            val proj = arrayOf("_data")
            context.contentResolver.query(this, proj, null, null)?.use {
                if (it.moveToFirst()) {
                    it.getStringOrNull(it.getColumnIndexOrThrow(proj[0])) ?: ""
                } else {
                    path
                }
            }
        }

        else -> path
    }

    LLogger.debug { "getAbsolutePath: path = $filePath" }
    return filePath ?: ""
}

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