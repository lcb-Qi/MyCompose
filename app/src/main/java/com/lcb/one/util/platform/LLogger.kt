package com.lcb.one.util.platform

import android.util.Log
import com.lcb.one.BuildConfig
import com.lcb.one.ui.MyApp
import com.lcb.one.R

object LLogger {
    private const val COMMON_TAG = "SaltFish#${BuildConfig.VERSION_NAME}"

    fun error(tag: String = "", lazyMsg: () -> Any) {
        println(Log.ERROR, buildLogMessage(tag, lazyMsg().toString()))
    }

    fun info(tag: String = "", lazyMsg: () -> Any) {
        println(Log.INFO, buildLogMessage(tag, lazyMsg().toString()))
    }

    fun warn(tag: String = "", lazyMsg: () -> Any) {
        println(Log.WARN, buildLogMessage(tag, lazyMsg().toString()))
    }

    fun debug(tag: String = "", lazyMsg: () -> Any) {
        println(Log.DEBUG, buildLogMessage(tag, lazyMsg().toString()))
    }

    fun verbose(tag: String = "", lazyMsg: () -> Any) {
        println(Log.VERBOSE, buildLogMessage(tag, lazyMsg().toString()))
    }

    private fun buildLogMessage(tag: String = "", vararg message: String): String {
        return if (message.isNotEmpty()) {
            val prefix = if (tag.isNotEmpty()) "[$tag] " else ""
            message.joinToString(separator = ", ", prefix = prefix)
        } else {
            ""
        }
    }

    private fun println(level: Int, msg: String) {
        println(COMMON_TAG, level, msg)
    }

    private fun println(tag: String = COMMON_TAG, level: Int, msg: String) {
        val len = msg.length
        var i = 0
        while (i < len) {
            val begin = i
            var end = begin + 3 * 1024 // log print has length limit
            if (end > len) {
                end = len
            }
            var content = msg.substring(begin, end)
            val nIdx = content.lastIndexOf(System.lineSeparator())
            if (nIdx > 0) {
                end = begin + nIdx + 1
                content = msg.substring(begin, end)
            }
            Log.println(level, tag, content)
            i = end
        }
    }
}