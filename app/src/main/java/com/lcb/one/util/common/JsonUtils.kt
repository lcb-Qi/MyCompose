package com.lcb.one.util.common

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object JsonUtils {
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())// 支持反射模式
            .build()
    }

    abstract class TypeToken<T>

    inline fun <reified T> getType(): Type {
        val typeToken = object : TypeToken<T>() {}

        return typeToken::class.java
            .genericSuperclass
            .let { it as ParameterizedType }
            .actualTypeArguments[0]
    }

    /************** inline 方式实现 *****************/
    inline fun <reified T> toJson(src: T?, indent: String = ""): String {
        return kotlin.runCatching {
            moshi.adapter<T>(getType<T>()).indent(indent).toJson(src)
        }.getOrDefault("")
    }

    /**
     * Json -> Bean
     * @param T 实体类，如果包含范型，请具体化，否则只会得到基本的list、map类型，而不是想要的实体类
     * @param src json字符串
     * @return 实例对象
     */
    inline fun <reified T> fromJson(src: String): T? {
        return kotlin.runCatching {
            moshi.adapter<T>(getType<T>()).fromJson(src)
        }.getOrNull()
    }


    /************** 不用 inline *****************/
    private fun <T> fromJson(src: String, typeOfT: Type): T? {
        val result = kotlin.runCatching {
            moshi.adapter<T>(typeOfT).fromJson(src)
        }

        return result.getOrNull()
    }

    fun <T> fromJson(src: String, typeOfT: Class<T>): T? {
        val result = kotlin.runCatching {
            moshi.adapter(typeOfT).fromJson(src)
        }

        return result.getOrNull()
    }

    fun <T> listFromJson(src: String, typeOfT: Class<T>): List<T>? {
        val realType = Types.newParameterizedType(List::class.java, typeOfT)

        return fromJson(src, realType)
    }

    fun <K, V> mapFromJson(src: String, typeOfKey: Class<K>, typeOfValue: Class<V>): Map<K, V>? {
        val realType = Types.newParameterizedType(Map::class.java, typeOfKey, typeOfValue)

        return fromJson(src, realType)
    }

    fun <T> toJson(src: T, type: Type, indent: String = ""): String {
        val result = kotlin.runCatching {
            val jsonAdapter = moshi.adapter<T>(type)
            jsonAdapter.indent(indent).toJson(src)
        }

        return result.getOrDefault("")
    }

    fun <T> listToJson(src: List<T>, typeOfT: Type, indent: String = ""): String {
        val result = kotlin.runCatching {
            val type = Types.newParameterizedType(List::class.java, typeOfT)
            val jsonAdapter = moshi.adapter<List<T>>(type)
            jsonAdapter.indent(indent).toJson(src)
        }

        return result.getOrDefault("")
    }

    fun <K, V> mapToJson(
        src: Map<K, V>,
        typeOfKey: Type,
        typeOfValue: Type,
        indent: String = ""
    ): String {
        val result = kotlin.runCatching {
            val type = Types.newParameterizedType(Map::class.java, typeOfKey, typeOfValue)
            val jsonAdapter = moshi.adapter<Map<K, V>>(type)
            jsonAdapter.indent(indent).toJson(src)
        }

        return result.getOrThrow()
    }
}