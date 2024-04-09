package com.lcb.one.util.android

import android.util.Log
import com.lcb.one.ui.MyApp
import com.lcb.one.R

object LLog {
    private val COMMON_TAG = "SaltFish#" + MyApp.getAppContext().getString(R.string.BUILD_ID)

    fun e(tag: String, vararg message: String) {
        println(Log.ERROR, buildLogMessage(tag, *message))
    }

    fun w(tag: String, vararg message: String) {
        println(Log.WARN, buildLogMessage(tag, *message))
    }

    fun i(tag: String, vararg message: String) {
        println(Log.INFO, buildLogMessage(tag, *message))
    }

    fun d(tag: String, vararg message: String) {
        println(Log.DEBUG, buildLogMessage(tag, *message))
    }

    fun v(tag: String, vararg message: String) {
        println(Log.VERBOSE, buildLogMessage(tag, *message))
    }

    private fun buildLogMessage(tag: String, vararg message: String): String {
        return if (message.isNotEmpty()) {
            message.joinToString(separator = ", ", prefix = "[$tag] ")
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