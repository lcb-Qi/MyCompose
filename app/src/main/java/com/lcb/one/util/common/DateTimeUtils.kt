package com.lcb.one.util.common

import com.lcb.one.R
import com.lcb.one.ui.MyApp
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private const val TAG = "DateTimeUtils"

    private val DEFAULT_ZONE_OFFSET: ZoneOffset = ZoneOffset.of("+8")
    private const val FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"
    private const val FORMAT_SHORT = "yyyyMMddHHmmss"

    fun nowMillis(): Long {
        return LocalDateTime.now().toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli()
    }

    fun nowString(): String {
        val formatter = DateTimeFormatter.ofPattern(FORMAT_DEFAULT)
        return LocalDateTime.now().format(formatter)
    }

    fun nowStringShort(): String {
        val formatter = DateTimeFormatter.ofPattern(FORMAT_SHORT)
        return LocalDateTime.now().format(formatter)
    }

    fun format(millis: Long, pattern: String = FORMAT_DEFAULT): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
        return localDateTime.format(formatter)
    }

    fun toMillis(text: String, pattern: String = FORMAT_DEFAULT): Long {
        val dateTime = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern))
        return dateTime.toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli()
    }

    fun friendlyDuration(start: Long, end: Long = nowMillis()): String {
        val startTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault())
        val endTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault())
        val duration = Duration.between(startTime, endTime)

        val daysPart = duration.toDaysPart()
        val hoursPart = duration.toHoursPart()
        val minutesPart = duration.toMinutesPart()
        val secondsPart = duration.toSecondsPart()

        return MyApp.getAppContext()
            .getString(R.string.friendly_duration, daysPart, hoursPart, minutesPart, secondsPart)
    }
}