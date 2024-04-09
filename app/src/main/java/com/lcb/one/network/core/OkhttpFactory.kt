package com.lcb.one.network.core

import com.lcb.one.util.android.LLog
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object OkhttpFactory {
    private val TIME_OUT = 10.seconds
    val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor {
            LLog.v("Okhttp", it)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .connectionPool(ConnectionPool())
            .connectTimeout(TIME_OUT.toJavaDuration())
            .readTimeout(TIME_OUT.toJavaDuration())
            .writeTimeout(TIME_OUT.toJavaDuration())
            .addInterceptor(loggingInterceptor)
            .build()
    }
}