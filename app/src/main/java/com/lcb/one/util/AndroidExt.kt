package com.lcb.one.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.database.getStringOrNull
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

const val ROOT_CONTENT_ID = Window.ID_ANDROID_CONTENT

// System Ui

/**
 * Immerse 沉浸。设置沉浸的方式
 *
 * @param type Type.systemBars(),Type.statusBars(),Type.navigationBars()
 * @param color 状态和导航栏的背景颜色
 */
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