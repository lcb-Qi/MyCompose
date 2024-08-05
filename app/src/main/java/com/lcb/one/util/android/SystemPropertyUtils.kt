package com.lcb.one.util.android

import android.annotation.SuppressLint

object SystemPropertyUtils {
    private const val TAG = "SystemPropertyUtils"
    private const val CLASS_NAME = "android.os.SystemProperties"

    @SuppressLint("PrivateApi")
    fun getString(name: String, default: String = ""): String {
        val result = runCatching {
            val systemProperties = Class.forName(CLASS_NAME)

            val methodOfGet =
                systemProperties.getMethod("get", String::class.java, String::class.java)

            methodOfGet.invoke(systemProperties, name, default) as? String?
        }.onFailure {
            LLogger.debug(TAG) { "getString[$name] failed: $it" }
        }

        return result.getOrNull() ?: default
    }
}