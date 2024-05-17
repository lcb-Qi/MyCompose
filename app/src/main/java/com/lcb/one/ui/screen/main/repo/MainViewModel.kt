package com.lcb.one.ui.screen.main.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.screen.main.repo.model.PoemResponse
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.UserPrefManager
import com.lcb.one.util.common.JsonUtils
import com.lcb.one.util.common.launchIOSafely
import com.lcb.one.util.common.launchSafely
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "PoemViewModel"
    }

    @Serializable
    data class PoemInfo(
        val recommend: String = "",
        val updateTime: Long = -1,
        val origin: PoemResponse.Data.Origin = PoemResponse.Data.Origin()
    )

    sealed class Event {
        data class Refresh(val force: Boolean = false) : Event()
        data object ShowDetail : Event()
        data object HideDetail : Event()
    }

    data class PoemState(val loading: Boolean, val poemInfo: PoemInfo, val showDetail: Boolean)

    var poemSate = MutableStateFlow(PoemState(false, PoemInfo(), false))

    init {
        viewModelScope.launchIOSafely {
            val last =
                JsonUtils.fromJson<PoemInfo>(UserPrefManager.getString(UserPrefManager.Key.POEM_LAST))
            if (last != null) with(last) {
                poemSate.update {
                    it.copy(poemInfo = this)
                }
            }
        }
    }

    fun sendEvent(event: Event) {
        LLog.d(TAG, "sendEvent: $event")
        when (event) {
            is Event.Refresh -> refresh(event.force)
            is Event.ShowDetail -> poemSate.update { it.copy(showDetail = true) }
            is Event.HideDetail -> poemSate.update { it.copy(showDetail = false) }
        }
    }


    private val poemService = PoemApiService.instance

    private suspend fun getToken(): String {
        var token = UserPrefManager.getString(UserPrefManager.Key.POEM_TOKEN)
        if (token.isNotEmpty()) return token
        token = poemService.getToken().token
        UserPrefManager.putString(UserPrefManager.Key.POEM_TOKEN, token)

        return token
    }

    private fun refresh(force: Boolean = false) {
        if (!needRefresh(force)) return

        viewModelScope.launchSafely {
            poemSate.update { it.copy(loading = true) }
            val poemResponse = withContext(Dispatchers.IO) {
                val token = getToken()
                poemService.getPoem(token)
            }
            poemSate.update {
                it.copy(
                    loading = false,
                    poemInfo = PoemInfo(
                        recommend = poemResponse.data.recommend,
                        updateTime = System.currentTimeMillis(),
                        origin = poemResponse.data.origin
                    )
                )
            }
            UserPrefManager.putString(
                UserPrefManager.Key.POEM_LAST,
                JsonUtils.toJson(poemSate.value.poemInfo)
            )
        }
    }

    private fun needRefresh(force: Boolean): Boolean {
        if (force) return true

        val info = poemSate.value.poemInfo
        return info.recommend.isBlank() ||
                (System.currentTimeMillis() - info.updateTime > AppGlobalConfigs.poemUpdateInterval)
    }
}