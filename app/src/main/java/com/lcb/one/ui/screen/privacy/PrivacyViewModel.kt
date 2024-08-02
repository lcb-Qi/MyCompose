package com.lcb.one.ui.screen.privacy

import androidx.lifecycle.ViewModel
import com.lcb.one.util.android.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class Privacy(
    val canReadImage: Boolean = false,
    val canAccessAllFile: Boolean = false,
    val canReadAudio: Boolean = false
)

class PrivacyViewModel : ViewModel() {
    companion object {
        private const val TAG = "PrivacyViewModel"
    }

    val privacyState: MutableStateFlow<Privacy> = MutableStateFlow(Privacy())

    fun checkPermission() {
        privacyState.update { last ->
            last.copy(
                canReadImage = PermissionUtils.canReadImage(),
                canAccessAllFile = PermissionUtils.canAccessAllFile(),
                canReadAudio = PermissionUtils.canReadAudio()
            )
        }
    }
}