package com.lcb.one.util.android

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.lcb.one.ui.MyApp

object ClipboardUtils {
    fun copyText(context: Context = MyApp.getAppContext(), text: String) {
        val clipboard = context.getSystemService(ClipboardManager::class.java)

        val plainText = ClipData.newPlainText(null, text)

        clipboard.setPrimaryClip(plainText)
    }
}