package com.lcb.one.network.core

import com.lcb.one.util.android.LLog
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkhttpFactory {
    private const val TIME_OUT = 10L/* seconds */
    private const val TAG = "OkhttpRequest"
    private val loggingInterceptor = HttpLoggingInterceptor {
        LLog.v(TAG, it)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(ConnectionPool())
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}