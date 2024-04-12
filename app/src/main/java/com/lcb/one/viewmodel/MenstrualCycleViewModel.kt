package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.bean.McDay
import com.lcb.one.room.appDatabase
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.launchSafely

class MenstrualCycleViewModel : ViewModel() {
    companion object {
        private const val TAG = "MenstrualCycleViewModel"
    }

    private val mcDayDao by lazy {
        appDatabase.getMcDayDao()
    }

    fun getAll() = mcDayDao.queryAll()

    fun startNewMenstrualCycle(startTime: Long) {
        LLog.d(TAG, "startNewMenstrualCycle: startTime = ${DateTimeUtils.toLocalDate(startTime)}")
        viewModelScope.launchSafely {
            mcDayDao.insert(McDay(startTime = startTime, finish = false))
        }
    }

    fun endMenstrualCycle(endTime: Long) {
        LLog.d(TAG, "endMenstrualCycle: endTime = ${DateTimeUtils.toLocalDate(endTime)}")
        viewModelScope.launchSafely {
            mcDayDao.getRunningMcDay()?.let {
                updateMenstrualCycle(it.copy(finish = true, endTime = endTime))
            }
        }
    }

    fun addPastMenstrualCycle(startTime: Long, endTime: Long) {
        viewModelScope.launchSafely {
            mcDayDao.insert(
                McDay(
                    finish = true,
                    startTime = startTime,
                    endTime = endTime
                )
            )
        }
    }

    fun updateMenstrualCycle(mcDay: McDay) {
        LLog.d(TAG, "updateMenstrualCycle: $mcDay")
        viewModelScope.launchSafely {
            mcDayDao.update(mcDay)
        }
    }

    fun deleteMenstrualCycle(mcDay: McDay) {
        LLog.d(TAG, "deleteMenstrualCycle: mcDay = $mcDay")
        viewModelScope.launchSafely {
            mcDayDao.delete(mcDay)
        }
    }
}