package com.lcb.one.util.common

import android.os.Build
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.isBeforeToday(): Boolean {
    return toLocalDate().isBefore(LocalDate.now())
}

fun LocalDateTime.isAfterToday(): Boolean {
    toLocalDate().isEqual(LocalDate.now())
    return toLocalDate().isAfter(LocalDate.now())
}

fun LocalDateTime.isToday(): Boolean {
    return toLocalDate().isEqual(LocalDate.now())
}

fun LocalDateTime.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atZone(zoneId).toInstant().toEpochMilli()
}

fun LocalDate.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atStartOfDay(zoneId).toInstant().toEpochMilli()
}

fun YearMonth.atDayMillis(day: Int): Long {
    require(day in 0..lengthOfMonth()) {
        "$year-$monthValue-$day is invalid, day must in ${0..lengthOfMonth()}"
    }

    return atDay(day).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

object DateTimeUtils {
    const val FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_SHORT = "yyyyMMddHHmmss"
    val FORMAT_ONLY_DATE
        get() =
            if (Locale.getDefault().language == Locale.CHINESE.language) {
                "yyyy年MM月dd日"
            } else {
                "yyyy-MM-dd"
            }


    fun toLocalDate(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            LocalDate.ofInstant(Instant.ofEpochMilli(millis), zoneId)
        } else {
            toLocalDateTime(millis, zoneId).toLocalDate()
        }
    }

    fun toLocalDateTime(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId)
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

    fun getDurationDays(start: Long, end: Long): Long {
        return Duration.between(toLocalDateTime(start), toLocalDateTime(end)).toDays()
    }

    fun isToday(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
        return toLocalDateTime(millis, zoneId).isToday()
    }

    fun isAfterToday(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
        return toLocalDateTime(millis, zoneId).isAfterToday()
    }

    fun isBeforeToday(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
        return toLocalDateTime(millis, zoneId).isBeforeToday()
    }
}