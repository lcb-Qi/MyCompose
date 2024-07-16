package com.lcb.one.ui.screen.privacy

import android.Manifest
import android.content.Context
import androidx.lifecycle.ViewModel
import com.lcb.one.ui.MyApp
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class Privacy(val readImg: Boolean = false, val allFile: Boolean = false)

class PrivacyViewModel : ViewModel() {
    companion object {
        private const val TAG = "PrivacyViewModel"
    }

    val privacyState: MutableStateFlow<Privacy> = MutableStateFlow(Privacy())

    fun checkPermission(context: Context = MyApp.get()) {
        LLog.d(TAG, "checkPermission: ")
        privacyState.update { last ->
            last.copy(
                readImg = PermissionUtils.hasPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ),
                allFile = PermissionUtils.hasAllFileAccess()
            )
        }
    }
}