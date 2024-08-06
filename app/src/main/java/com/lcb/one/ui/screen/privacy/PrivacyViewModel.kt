package com.lcb.one.ui.screen.privacy

import android.Manifest
import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.lcb.one.ui.MyApp
import com.lcb.one.util.android.AppUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class Privacy(
    val canReadImage: Boolean = false,
    val canAccessAllFile: Boolean = false,
    val canReadAudio: Boolean = false
)

class PrivacyViewModel : ViewModel() {
    val privacyState: MutableStateFlow<Privacy> = MutableStateFlow(Privacy())

    fun checkPermission() {
        privacyState.update { last ->
            last.copy(
                canReadImage = canReadImage(),
                canAccessAllFile = canAccessAllFile(),
                canReadAudio = canReadAudio()
            )
        }
    }

    private fun canReadAudio(context: Context = MyApp.get()) =
        AppUtils.hasPermission(context, Manifest.permission.READ_MEDIA_AUDIO)

    private fun canReadImage(context: Context = MyApp.get()) =
        AppUtils.hasPermission(context, Manifest.permission.READ_MEDIA_IMAGES)

    private fun canAccessAllFile() = Environment.isExternalStorageManager()
}