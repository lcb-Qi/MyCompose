package com.lcb.one.ui.screen.bilibili.repo

import com.lcb.one.network.CommonApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object BiliServerAccessor {
    private const val TAG = "BiliServerAccessor"
    private const val BASE_URL = "https://api.bilibili.com/"
    private const val VIDEO_INO_URL = BASE_URL + "x/web-interface/view"

    suspend fun getVideoCoverUrl(videoId: String) = withContext(Dispatchers.IO) {
        val key = if (videoId.startsWith("av")) "avid" else "bvid"
        val params = mapOf(key to videoId)

        val response = CommonApiService.instance.get(VIDEO_INO_URL, params)
        return@withContext JSONObject(response).optJSONObject("data")?.optString("pic") ?: ""
    }
}