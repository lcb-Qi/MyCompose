package com.lcb.one.ui.screen.menstruationAssistant.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.database.appDatabase
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.LLogger
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.JsonUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okio.buffer
import okio.source
import java.io.InputStream

class MenstruationViewModel : ViewModel() {
    companion object {
        private const val TAG = "MenstruationViewModel"
    }

    private val dao by lazy { appDatabase.getMenstruationDayDao() }

    val all = dao.queryAll()

    fun import(input: InputStream) {
        viewModelScope.launch {
            runCatching {
                input.source().buffer().use { reader ->
                    val text = reader.readString(Charsets.UTF_8).replace("\\s".toRegex(), "")
                    val data = JsonUtils.fromJson<List<MenstruationDay>>(text)
                    data.forEach { dao.insert(it) }
                }
            }.onFailure {
                LLogger.debug(TAG) { "importFromFile failed: $it" }
            }
        }
    }

    fun export() {
        viewModelScope.launch {
            all.collectLatest { data ->
                if (data.isEmpty()) return@collectLatest
                runCatching {
                    val json = JsonUtils.toJson(data).replace("\\s".toRegex(), "")
                    val filename = "salt_fish_backup_${DateTimeUtils.nowStringShort()}.txt"
                    StorageUtils.createDocument(json, filename)
                }.onFailure {
                    LLogger.debug(TAG) { "export failed: $it" }
                }
            }
        }
    }

    fun startNewMenstruationDay(startTime: Long) {
        LLogger.debug(TAG) {
            "startNewMenstruation: startTime = ${
                DateTimeUtils.toLocalDate(
                    startTime
                )
            }"
        }
        viewModelScope.launch {
            dao.insert(MenstruationDay(startTime = startTime, finish = false))
        }
    }

    fun endMenstruationDay(endTime: Long) {
        LLogger.debug(TAG) { "endMenstruation: endTime = ${DateTimeUtils.toLocalDate(endTime)}" }
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
        LLogger.debug(TAG) { "updateMenstruationDay: $mcDay" }
        viewModelScope.launch {
            dao.update(mcDay)
        }
    }

    fun deleteMenstruationDay(mcDay: MenstruationDay) {
        LLogger.debug(TAG) { "deleteMenstruationDay: mcDay = $mcDay" }
        viewModelScope.launch {
            dao.delete(mcDay)
        }
    }
}