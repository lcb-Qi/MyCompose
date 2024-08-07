package com.lcb.one.network.core

import android.annotation.SuppressLint
import com.lcb.one.util.android.LLogger
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object OkhttpFactory {
    private val TIME_OUT = 10.seconds.toJavaDuration()
    private const val TAG = "OkhttpRequest"

    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                connectionPool(ConnectionPool())
                connectTimeout(TIME_OUT)
                readTimeout(TIME_OUT)
                writeTimeout(TIME_OUT)

                val trustManager = DefaultTrustManager()
                sslSocketFactory(createSSLFactory(trustManager), trustManager)
                hostnameVerifier(createHostnameVerifier())

                addInterceptor(createLogger())
            }
            .build()
    }

    private fun createHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, _ ->
            LLogger.verbose(TAG) { "hostnameVerifier: hostname = $hostname" }
            true
        }
    }

    private fun createLogger(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { LLogger.verbose(TAG) { it } }
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
            LLogger.verbose(TAG) { "checkClientTrusted: authType = $authType" }
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            LLogger.verbose(TAG) { "checkServerTrusted: authType = $authType" }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            LLogger.verbose(TAG) { "getAcceptedIssuers: " }

            return arrayOfNulls(0)
        }
    }
}