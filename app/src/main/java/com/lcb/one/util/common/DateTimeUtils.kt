package com.lcb.one.util.common

import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils.toMillis
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTimeUtils {
    const val FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_SHORT = "yyyyMMddHHmmss"
    const val FORMAT_ONLY_DATE = "yyyy-MM-dd"

    fun LocalDateTime.isToday(): Boolean {
        val now = LocalDate.now().atTime(0, 0, 0)
        val toDays = Duration.between(this, now).toDays()

        return toDays == 0L
    }

    fun LocalDateTime.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
        return atZone(zoneId).toInstant().toEpochMilli()
    }

    fun LocalDate.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
        return atStartOfDay(zoneId).toInstant().toEpochMilli()
    }

    fun toLocalDateTime(millis: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
    }

    fun nowMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
        return LocalDateTime.now().atZone(zoneId).toInstant().toEpochMilli()
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
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
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