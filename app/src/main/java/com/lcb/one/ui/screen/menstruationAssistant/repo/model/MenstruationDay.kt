package com.lcb.one.ui.screen.menstruationAssistant.repo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.toMillis
import java.time.LocalDate

@Entity(
    tableName = "mc_day",
    indices = [
        Index(value = ["start_time"], unique = true)
    ]
)
data class MenstruationDay(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    val finish: Boolean = false,
    @ColumnInfo(name = "start_time", defaultValue = "${Long.MIN_VALUE}")
    val startTime: Long = Long.MIN_VALUE,// LocalDate对应的时间戳，仅包含年月日
    @ColumnInfo(name = "end_time", defaultValue = "${Long.MIN_VALUE}")
    val endTime: Long = Long.MIN_VALUE// LocalDate对应的时间戳，仅包含年月日
) {
    fun getDurationDay(): Long {
        return if (finish) {
            DateTimeUtils.getDurationDays(startTime, endTime) + 1
        } else {
            DateTimeUtils.getDurationDays(startTime, DateTimeUtils.nowMillis()) + 1
        }
    }

    /**
     * 已结束：起止时间
     *
     * 未结束：开始时间到今天
     */
    fun toRange() = if (finish) {
        startTime..endTime
    } else {
        startTime..LocalDate.now().toMillis()
    }

}

fun List<MenstruationDay>.getMcDay(time: Long): MenstruationDay? {
    return find {
        val range = if (it.finish) {
            it.startTime..it.endTime
        } else {
            it.startTime..LocalDate.now().toMillis()
        }

        return@find time in range
    }
}

fun List<MenstruationDay>.allFinished() = all { it.finish }

fun List<MenstruationDay>.averageDurationDay(): Float {
    val finished = filter { it.finish }
    if (finished.isEmpty()) return 0f

    val sumOf = finished.sumOf { it.getDurationDay() }

    return (sumOf.toFloat() / finished.size.toFloat())
}

fun List<MenstruationDay>.averageIntervalDay(): Float {
    if (size <= 1) return 0f

    var total = 0f
    var last = first()
    for (index in indices) {
        if (index != 0) {
            val mcDay = this[index]
            total += DateTimeUtils.getDurationDays(last.startTime, mcDay.startTime)

            last = mcDay
        }
    }

    return total / (size - 1).toFloat()
}