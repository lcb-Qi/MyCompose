package com.lcb.one.util.common

import com.lcb.one.util.android.LLog
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object JsonUtils {
    const val TAG = "JsonUtils"

    val json by lazy {
        Json(from = Json) {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    fun getConverterFactory() =
        json.asConverterFactory("application/json; charset=UTF8".toMediaType())

    inline fun <reified T> toJson(src: T, default: String = ""): String {
        return try {
            json.encodeToString(src)
        } catch (e: Exception) {
            LLog.d(TAG, "toJson failed: $e")
            default
        }
    }

    inline fun <reified T> fromJson(src: String, default: T? = null): T? {
        return try {
            json.decodeFromString<T>(src)
        } catch (e: Exception) {
            LLog.d(TAG, "fromJson failed: $e")
            default
        }
    }
}