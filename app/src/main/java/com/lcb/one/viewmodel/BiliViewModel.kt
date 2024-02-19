package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.network.BiliServerAccessor
import com.lcb.one.util.LLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BiliViewModel : ViewModel() {
    private val TAG = "BiliViewModel"
    private val biliService = BiliServerAccessor.getService()

    val coverUrl = MutableStateFlow("")

    fun getVideoInfo(videoId: String) {
        LLog.d(TAG, "getVideoInfo: videoId = $videoId")
        viewModelScope.launch {
            val videoInfoResponse = if (videoId.startsWith("av", ignoreCase = true)) {
                biliService.getVideoInfoByAvId(videoId)
            } else {
                biliService.getVideoInfoByBvId(videoId)
            }

            coverUrl.value = videoInfoResponse.data.pic
        }
    }
}