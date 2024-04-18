package com.lcb.one.ui.screen.main.repo

import com.lcb.one.network.RetrofitHelper
import com.lcb.one.ui.screen.main.repo.model.PoemResponse
import com.lcb.one.ui.screen.main.repo.model.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface PoemApiService {
    companion object {
        private const val BASE_URL = "https://v2.jinrishici.com/"

        val instance: PoemApiService by lazy {
            RetrofitHelper.createApiService(BASE_URL, PoemApiService::class.java)
        }
    }

    // https://v2.jinrishici.com/token
    @GET("token")
    suspend fun getToken(): TokenResponse

    // https://v2.jinrishici.com/sentence
    @GET("sentence")
    suspend fun getPoem(@Header("X-User-Token") token: String): PoemResponse
}