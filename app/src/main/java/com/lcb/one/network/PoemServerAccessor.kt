package com.lcb.one.network

import com.lcb.one.bean.TokenResponse
import com.lcb.one.network.os.OkhttpFactory
import com.lcb.one.bean.PoemResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

class PoemServerAccessor private constructor() {
    companion object {
        private const val BASE_URL = "https://v2.jinrishici.com/"

        fun getService(): PoemService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkhttpFactory.okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(PoemService::class.java)
        }
    }


    interface PoemService {
        // https://v2.jinrishici.com/token
        @GET("token")
        suspend fun getToken(): TokenResponse

        // https://v2.jinrishici.com/sentence
        @GET("sentence")
        suspend fun getPoem(@Header("X-User-Token") token: String): PoemResponse
    }
}