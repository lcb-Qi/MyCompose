package com.lcb.one.network.core

import androidx.annotation.StringDef
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class RESTFulRequest private constructor(private val builder: Builder) {
    companion object {
        private const val TAG = "RESTFulRequest"
        fun newBuilder(url: String) = Builder().url(url)
    }

    private fun createRequest(): Request {
        val requestBuilder = Request.Builder().apply {
            url(builder.url)
            when (builder.method) {
                RequestMethod.GET -> {
                    url(buildUrl(builder))
                    get()
                }

                RequestMethod.POST -> post(buildRequestBody(builder))
                RequestMethod.PATCH -> patch(buildRequestBody(builder))
                RequestMethod.PUT -> put(buildRequestBody(builder))
                RequestMethod.DELETE -> delete(buildRequestBody(builder))
                else -> {}
            }

            builder.headers.forEach { (key, value) ->
                addHeader(key, value)
            }
        }

        return requestBuilder.build()
    }

    private fun buildRequestBody(builder: Builder): RequestBody {
        val requestBody =
            if (builder.isJson) {
                val jsonBody = JsonUtils.toJson(builder.params)
                jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
            } else {
                val formBody = FormBody.Builder()
                builder.params.forEach { (key, value) ->
                    formBody.add(key, value)
                }
                formBody.build()
            }
        return requestBody
    }

    private fun buildUrl(builder: Builder): HttpUrl {
        val urlBuilder = builder.url.toHttpUrl().newBuilder()
        builder.params.forEach { (key, value) ->
            urlBuilder.addQueryParameter(key, value)
        }

        return urlBuilder.build()
    }

    suspend fun <T> getResult(type: Class<T>): Result<T?> = withContext(Dispatchers.IO) {
        val request = createRequest()

        val result = runCatching {
            val response = OkhttpFactory.okHttpClient.newCall(request).execute()
            val body = response.body?.string()
            // 允许直接返回String
            if (type.name.endsWith("String")) {
                return@runCatching body as? T
            } else {
                return@runCatching body?.let {
                    JsonUtils.fromJson(it, type)
                }
            }
        }

        result.onFailure {
            if (it is IOException) {
                LLog.w(TAG, "getResult failed: $it")
            } else {
                throw it
            }
        }
        return@withContext result
    }

    class Builder {
        @RequestMethod
        var method: String = RequestMethod.GET
            private set
        var url: String = ""
            private set
        val headers: MutableMap<String, String> = mutableMapOf()
        val params: MutableMap<String, String> = mutableMapOf()
        var isJson: Boolean = false
            private set

        fun param(key: String, value: String) = apply {
            if (key.isNotBlank()) {
                this.params[key] = value
            }
        }

        fun url(url: String) = apply {
            this.url = url
        }

        fun get() = apply {
            this.method = RequestMethod.GET
        }

        fun post(isJson: Boolean = true) = apply {
            this.method = RequestMethod.POST
            this.isJson = isJson
        }

        fun put() = apply {
            this.method = RequestMethod.PUT
        }

        fun delete() = apply {
            this.method = RequestMethod.DELETE
        }

        fun patch() = apply {
            this.method = RequestMethod.PATCH
        }

        fun header(key: String, value: String) = apply {
            this.headers[key] = value
        }

        fun header(headers: Map<String, String>) = apply {
            this.headers.putAll(headers)
        }

        fun build() = RESTFulRequest(this)
    }


    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.DELETE,
        RequestMethod.PATCH,
        RequestMethod.PUT
    )
    annotation class RequestMethod {
        companion object {
            const val GET = "GET"
            const val POST = "POST"
            const val DELETE = "DELETE"
            const val PATCH = "PATCH"
            const val PUT = "PUT"
        }
    }
}
