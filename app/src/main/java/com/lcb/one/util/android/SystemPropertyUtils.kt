package com.lcb.one.util.android

import android.annotation.SuppressLint

object SystemPropertyUtils {
    private const val CLASS_NAME = "android.os.SystemProperties"

    @SuppressLint("PrivateApi")
    fun getString(name: String, default: String = ""): String {
        val runCatching = runCatching {
            val systemProperties = Class.forName(CLASS_NAME)

            val paramTypes = arrayOf(String::class.java, String::class.java)
            val get = systemProperties.getMethod("get", *paramTypes)

            val params = arrayOf(name, default)
            get.invoke(systemProperties, *params) as? String
        }


        return runCatching.getOrNull() ?: default
    }
}