package com.lcb.one.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BiliServerAccessor {
    private const val TAG = "BiliServerAccessor"
    private const val BASE_URL = "https://api.bilibili.com/"
    private const val VIDEO_INO_URL = BASE_URL + "x/web-interface/view"

    suspend fun getVideoInfo(videoId: String) = withContext(Dispatchers.IO) {
        val key = if (videoId.startsWith("av")) "avid" else "bvid"
        val params = mapOf(key to videoId)

        return@withContext commonApiService.get(VIDEO_INO_URL, params)
    }
}