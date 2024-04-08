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
    @ColumnInfo(name = "start_time", defaultValue = "NULL")
    val startTime: Long? = null,// LocalDate对应的时间戳，仅包含年月日
    @ColumnInfo(name = "end_time", defaultValue = "NULL")
    val endTime: Long? = null// LocalDate对应的时间戳，仅包含年月日
)
