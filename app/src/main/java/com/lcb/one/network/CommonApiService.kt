package com.lcb.one.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import retrofit2.http.Url

interface CommonApiService {
    companion object {
        val instance: CommonApiService by lazy {
            RetrofitHelper.createApiService(BASE_URL, CommonApiService::class.java)
        }
        private const val BASE_URL = "https://127.0.0.1:8080/"
    }

    /**
     * 发起 GET 请求
     * @param url 完整的 url 路径
     * @param params query 参数，也可以自行拼接在 url 中
     * @return
     */
    @GET
    suspend fun get(@Url url: String, @QueryMap params: Map<String, String> = emptyMap()): String

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody
}