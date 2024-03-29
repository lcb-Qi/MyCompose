package com.lcb.one.util.android

import android.util.Log
import com.lcb.one.ui.MyApp
import com.lcb.one.R

object LLog {
    private val TAG = "LLog#" + MyApp.getAppContext().getString(R.string.BUILD_ID)
    private val LINE_SEPARATOR = System.lineSeparator()

    fun e(tag: String, message: String) {
        println(Log.ERROR, buildLogMessage(tag, message))
    }

    fun w(tag: String, message: String) {
        println(Log.WARN, buildLogMessage(tag, message))
    }

    fun i(tag: String, message: String) {
        println(Log.INFO, buildLogMessage(tag, message))
    }

    fun d(tag: String, message: String) {
        println(Log.DEBUG, buildLogMessage(tag, message))
    }

    fun v(tag: String, message: String) {
        println(Log.VERBOSE, buildLogMessage(tag, message))
    }


    private fun buildLogMessage(tag: String, vararg message: String): String {
        return if (message.isNotEmpty()) {
            val builder = StringBuilder()
            builder.append("[")
            builder.append(tag)
            builder.append("] >> ")
            for (i in message.indices) {
                if (i != 0) {
                    builder.append(", ")
                }
                builder.append(message[i])
            }
            builder.toString()
        } else {
            ""
        }
    }

    private fun println(level: Int, msg: String) {
        println(TAG, level, msg)
    }

    private fun println(tag: String, level: Int, msg: String) {
        val len = msg.length
        var i = 0
        while (i < len) {
            val begin = i
            var end = begin + 3 * 1024 // log print has length limit
            if (end > len) {
                end = len
            }
            var content = msg.substring(begin, end)
            val nIdx = content.lastIndexOf(LINE_SEPARATOR)
            if (nIdx > 0) {
                end = begin + nIdx + 1
                content = msg.substring(begin, end)
            }
            Log.println(level, tag, content)
            i = end
        }
    }
}