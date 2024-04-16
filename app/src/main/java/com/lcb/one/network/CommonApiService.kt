package com.lcb.one.network

import com.lcb.one.network.core.OkhttpFactory
import com.lcb.one.util.common.JsonUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import retrofit2.http.Url

val commonApiService: CommonApiService by lazy {
    Retrofit.Builder()
        .baseUrl("https://127.0.0.1:8080/")
        .client(OkhttpFactory.okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(JsonUtils.getConverterFactory())
        .build()
        .create(CommonApiService::class.java)
}

interface CommonApiService {
    @GET
    suspend fun get(@Url url: String, @QueryMap params: Map<String, String> = emptyMap()): String

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody

}