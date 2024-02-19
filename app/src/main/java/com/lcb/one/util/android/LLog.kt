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
                builder.append(formatString(message[i]))
            }
            builder.toString()
        } else {
            ""
        }
    }

    private fun formatString(text: String): String {
        return if (text.contains("{") && text.contains("}")) {
            formatJson(text)
        } else if (text.contains("[") && text.contains("]")) {
            formatJson(text)
        } else {
            text
        }
    }

    private fun formatJson(json: String): String {
        if (json.length <= 200) return json

        val result = StringBuffer()
        val length = json.length
        var number = 0
        var previous: Char? = null
        var current = 0.toChar()
        var next: Char? = null

        // 遍历输入字符串。
        for (i in 0 until length) {
            // 1、获取当前字符。
            previous = if (i == 0) null else json[i - 1]
            current = json[i]
            next = if (i == length - 1) null else json[i + 1]

            // 2、如果当前字符是前方括号、前花括号做如下处理：
            if (current == '[' || current == '{') {
                // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if (previous == ':' && next != ']' && next != '}') {
                    result.append(LINE_SEPARATOR)
                    result.append(indent(number))
                }
                // （2）打印：当前字符。
                result.append(current)

                // （3）前方括号、前花括号，的后面必须换行。打印：换行。
                if (next != '}' && next != ']') {
                    result.append(LINE_SEPARATOR)
                }

                // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++
                if (next != ']' && next != '}') {
                    result.append(indent(number))
                }

                // （5）进行下一次循环。
                continue
            }

            // 3、如果当前字符是后方括号、后花括号做如下处理：
            if (current == ']' || current == '}') {
                // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                if (previous != ']' && previous != '}'
                    && previous != '[' && previous != '{'
                ) {
                    result.append(LINE_SEPARATOR)
                }

                // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--
                if (previous != '[' && previous != '{') {
                    result.append(indent(number))
                }

                // （3）打印：当前字符。
                result.append(current)

                // （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (next != ',') {
                    result.append(LINE_SEPARATOR)
                }

                // （5）继续下一次循环。
                continue
            }

            // 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if (current == ',') {
                result.append(current)
                result.append(LINE_SEPARATOR)
                result.append(indent(number))
                continue
            }

            // 5、打印：当前字符。
            result.append(current)
        }
        return result.toString()
    }

    private fun indent(number: Int): String {
        return "   ".repeat(number)
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