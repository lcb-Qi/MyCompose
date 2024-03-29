package com.lcb.one.network

import com.lcb.one.network.os.RESTFulRequest
import com.lcb.one.util.android.LLog

object BiliServerAccessor {
    private const val TAG = "BiliServerAccessor"
    private const val BASE_URL = "https://api.bilibili.com/"

    suspend fun getVideoInfo(videoId: String): String? {
        val key = if (videoId.startsWith("av")) "avid" else "bvid"
        val result = RESTFulRequest.newBuilder(BASE_URL + "x/web-interface/view")
            .param(key, videoId)
            .build()
            .getResult(String::class.java)

        return result.onFailure {
            LLog.d(TAG, "getVideoInfo failed: $it")
        }.getOrNull()
    }
}