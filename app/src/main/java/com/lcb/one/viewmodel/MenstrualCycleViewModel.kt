package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.bean.McDay
import com.lcb.one.room.appDatabase
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils.toMillis
import kotlinx.coroutines.launch
import java.time.LocalDate

class MenstrualCycleViewModel : ViewModel() {
    companion object {
        private const val TAG = "MenstrualCycleViewModel"
    }

    private val mcDayDao by lazy {
        appDatabase.getMcDayDao()
    }

    val mcDaysFlow = mcDayDao.queryAll()

    fun startNewMenstrualCycle() {
        LLog.d(TAG, "startNewMenstrualCycle: ")
        viewModelScope.launch {
            val nowMillis = LocalDate.now().toMillis()
            mcDayDao.insert(McDay(startTime = nowMillis, finish = false))
        }
    }

    fun endMenstrualCycle(endTime: Long) {
        LLog.d(TAG, "endMenstrualCycle: ")
        viewModelScope.launch {
            mcDayDao.getRunningMcDay()?.let {
                updateMenstrualCycle(it.copy(finish = true, endTime = endTime))
            }
        }
    }

    fun updateMenstrualCycle(mcDay: McDay) {
        LLog.d(TAG, "updateMenstrualCycle: $mcDay")
        viewModelScope.launch {
            mcDayDao.update(mcDay)
        }
    }
}