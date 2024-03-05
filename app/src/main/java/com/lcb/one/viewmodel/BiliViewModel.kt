package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.network.BiliServerAccessor
import com.lcb.one.util.android.LLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BiliViewModel : ViewModel() {
    private val TAG = "BiliViewModel"
    val coverUrl = MutableStateFlow("")

    val isLoading = MutableStateFlow(false)

    fun getCoverUrl(videoId: String) {
        LLog.d(TAG, "getCoverUrl: videoId = $videoId")
        viewModelScope.launch {
            isLoading.value = true
            BiliServerAccessor.getVideoInfo(videoId)?.let {
                LLog.d(TAG, "getCoverUrl: it.data.pic = ${it.data.pic}")
                coverUrl.value = it.data.pic
            }
            isLoading.value = false
        }
    }
}