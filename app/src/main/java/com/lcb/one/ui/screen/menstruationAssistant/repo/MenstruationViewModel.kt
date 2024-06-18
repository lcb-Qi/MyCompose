package com.lcb.one.ui.screen.menstruationAssistant.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.database.appDatabase
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.util.android.LLog
import com.lcb.one.util.common.DateTimeUtils
import kotlinx.coroutines.launch

class MenstruationViewModel : ViewModel() {
    companion object {
        private const val TAG = "MenstruationViewModel"
    }

    private val dao by lazy { appDatabase.getMenstruationDayDao() }

    fun getAll() = dao.queryAll()

    fun startNewMenstruationDay(startTime: Long) {
        LLog.d(TAG, "startNewMenstruation: startTime = ${DateTimeUtils.toLocalDate(startTime)}")
        viewModelScope.launch {
            dao.insert(MenstruationDay(startTime = startTime, finish = false))
        }
    }

    fun endMenstruationDay(endTime: Long) {
        LLog.d(TAG, "endMenstruation: endTime = ${DateTimeUtils.toLocalDate(endTime)}")
        viewModelScope.launch {
            dao.getRunningMcDay()?.let {
                updateMenstruationDay(it.copy(finish = true, endTime = endTime))
            }
        }
    }

    fun addPastMenstruationDay(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            dao.insert(
                MenstruationDay(
                    finish = true,
                    startTime = startTime,
                    endTime = endTime
                )
            )
        }
    }

    fun updateMenstruationDay(mcDay: MenstruationDay) {
        LLog.d(TAG, "updateMenstruationDay: $mcDay")
        viewModelScope.launch {
            dao.update(mcDay)
        }
    }

    fun deleteMenstruationDay(mcDay: MenstruationDay) {
        LLog.d(TAG, "deleteMenstruationDay: mcDay = $mcDay")
        viewModelScope.launch {
            dao.delete(mcDay)
        }
    }
}