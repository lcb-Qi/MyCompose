package com.lcb.one.util.common

import com.lcb.one.util.android.LLogger
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonUtils {
    private const val TAG = "JsonUtils"

    val json by lazy {
        Json(from = Json) {
            ignoreUnknownKeys = true
            // prettyPrint = true
        }
    }

    @Throws(SerializationException::class, IllegalArgumentException::class)
    inline fun <reified T> toJson(src: T) = json.encodeToString(src)

    fun <T> toJsonOrDefault(
        src: T,
        serializer: SerializationStrategy<T>,
        default: String = ""
    ): String {
        return try {
            json.encodeToString(serializer, src)
        } catch (e: Exception) {
            LLogger.debug(TAG) { "toJsonOrDefault failed: src = ${src.toString()}" }
            e.printStackTrace()
            default
        }
    }

    @Throws(SerializationException::class, IllegalArgumentException::class)
    inline fun <reified T> fromJson(src: String) = json.decodeFromString<T>(src)

    fun <T> fromJsonOrDefault(
        src: String,
        deserializer: DeserializationStrategy<T>,
        default: T? = null
    ): T? {
        return try {
            json.decodeFromString(deserializer, src)
        } catch (e: Exception) {
            LLogger.debug(TAG) { "fromJsonOrDefault failed: src = $src" }
            e.printStackTrace()
            default
        }
    }
}