package com.lcb.one.ui.screen.menstruationAssistant.repo

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.database.appDatabase
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.LLog
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

    fun importFromFile(input: InputStream) {
        viewModelScope.launch {
            runCatching {
                input.source().buffer().use { reader ->
                    val text = reader.readString(Charsets.UTF_8).replace("\\s".toRegex(), "")
                    val data = JsonUtils.fromJson<List<MenstruationDay>>(text)
                    data.fastForEach { dao.insert(it) }
                }
            }.onFailure {
                LLog.d(TAG, "importFromFile failed: $it")
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
                    LLog.d(TAG, "export failed: $it")
                }
            }
        }
    }

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