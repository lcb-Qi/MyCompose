package com.lcb.one.network

import com.lcb.one.network.core.OkhttpFactory
import com.lcb.one.util.common.JsonUtils
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitHelper {
    fun <T> createApiService(
        baseUrl: String,
        clazz: Class<T>,
        client: OkHttpClient = OkhttpFactory.okHttpClient,
        vararg converterFactory: Converter.Factory
    ): T {
        val builder = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(client)
            addConverterFactory(ScalarsConverterFactory.create())
            addConverterFactory(JsonUtils.getConverterFactory())
            converterFactory.forEach {
                addConverterFactory(it)
            }
        }

        return builder.build().create(clazz)
    }
}