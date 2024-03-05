package com.lcb.one.network

import com.lcb.one.bean.PoemResponse
import com.lcb.one.bean.TokenResponse
import com.lcb.one.network.os.RESTFulRequest
import com.lcb.one.util.android.LLog


object PoemServerAccessor {
    private const val TAG = "PoemServerAccessor"
    private const val BASE_URL = "https://v2.jinrishici.com/"

    suspend fun getToken(): TokenResponse? {
        val result =
            RESTFulRequest.create(BASE_URL + "token") {}.getResult(TokenResponse::class.java)

        return result.getOrNull()
    }

    suspend fun getPoem(token: String): PoemResponse? {
        val result = RESTFulRequest.create(BASE_URL + "sentence") {
            header("X-User-Token", token)
        }.getResult(PoemResponse::class.java)

        return result.getOrNull()
    }
}