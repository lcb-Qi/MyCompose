package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.bean.McDay
import com.lcb.one.room.appDatabase
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils.toMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MenstrualCycleViewModel : ViewModel() {
    companion object {
        private const val TAG = "MenstrualCycleViewModel"
    }

    private val mcDayDao by lazy {
        appDatabase.getMcDayDao()
    }

    val mcDaysFlow by lazy {
        MutableStateFlow<List<McDay>>(emptyList())
    }

    fun getMcDays(): Flow<List<McDay>> {
        LLog.d(TAG, "getMcDays: ")
        return mcDayDao.queryAll()
    }

    fun add(mcDay: McDay) {
        LLog.d(TAG, "add: $mcDay")
        viewModelScope.launch {
            mcDayDao.insert(mcDay)
        }
    }

    fun getByTime(date: LocalDate): McDay? {
        return mcDayDao.getByTime(date.toMillis())
    }
}