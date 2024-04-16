package com.lcb.one.ui.screen.main.repo

import com.lcb.one.network.core.OkhttpFactory
import com.lcb.one.ui.screen.main.repo.model.PoemResponse
import com.lcb.one.ui.screen.main.repo.model.TokenResponse
import com.lcb.one.util.common.JsonUtils
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header

class PoemServerAccessor private constructor() {
    companion object {
        private const val BASE_URL = "https://v2.jinrishici.com/"

        val apiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkhttpFactory.okHttpClient)
                .addConverterFactory(JsonUtils.getConverterFactory())
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