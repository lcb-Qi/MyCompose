package com.lcb.one.network.core

import android.annotation.SuppressLint
import com.lcb.one.util.android.LLog
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object OkhttpFactory {
    private const val TIME_OUT = 10L/* seconds */
    private const val TAG = "OkhttpRequest"

    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                connectionPool(ConnectionPool())
                connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                readTimeout(TIME_OUT, TimeUnit.SECONDS)
                writeTimeout(TIME_OUT, TimeUnit.SECONDS)

                val trustManager = DefaultTrustManager()
                sslSocketFactory(createSSLFactory(trustManager), trustManager)
                hostnameVerifier(createHostnameVerifier())

                addInterceptor(createLogger())
            }
            .build()
    }

    private fun createHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, _ ->
            LLog.v(TAG, "hostnameVerifier: hostname = $hostname")
            true
        }
    }

    private fun createLogger(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { LLog.v(TAG, it) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggingInterceptor
    }

    private fun createSSLFactory(vararg tm: TrustManager): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tm, SecureRandom())

        return sslContext.socketFactory
    }

    @SuppressLint("CustomX509TrustManager")
    private class DefaultTrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            LLog.v(TAG, "checkClientTrusted: authType = $authType")
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            LLog.v(TAG, "checkServerTrusted: authType = $authType")
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            LLog.v(TAG, "getAcceptedIssuers: ")

            return arrayOfNulls(0)
        }
    }
}