package com.lcb.one.util.platform

import android.annotation.SuppressLint

object SystemPropUtils {
    private const val TAG = "SystemPropUtils"
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