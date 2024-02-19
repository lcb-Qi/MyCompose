package com.lcb.one.network

import com.lcb.one.network.os.OkhttpFactory
import com.lcb.one.bean.VideoInfoResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class BiliServerAccessor private constructor() {
    companion object {
        private const val BASE_URL = "https://api.bilibili.com/"

        fun getService(): BiliService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkhttpFactory.okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(BiliService::class.java)
        }
    }


    interface BiliService {
        @GET("x/web-interface/view")
        suspend fun getVideoInfoByAvId(@Query("avid") avId: String): VideoInfoResponse

        @GET("x/web-interface/view")
        suspend fun getVideoInfoByBvId(@Query("bvid") avId: String): VideoInfoResponse
    }
}