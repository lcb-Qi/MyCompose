package com.lcb.one.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mc_day",
    indices = [
        Index(value = ["start_time"], unique = true)
    ]
)
data class McDay(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    val finish: Boolean = false,
    @ColumnInfo(name = "start_time", defaultValue = "${Long.MIN_VALUE}")
    val startTime: Long = Long.MIN_VALUE,// LocalDate对应的时间戳，仅包含年月日
    @ColumnInfo(name = "end_time", defaultValue = "${Long.MIN_VALUE}")
    val endTime: Long = Long.MIN_VALUE// LocalDate对应的时间戳，仅包含年月日
)
