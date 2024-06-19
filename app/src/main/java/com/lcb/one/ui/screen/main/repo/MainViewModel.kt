package com.lcb.one.ui.screen.main.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.screen.main.repo.model.PoemInfo
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.UserPrefManager
import com.lcb.one.util.common.ExceptionHandler
import com.lcb.one.util.common.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "PoemViewModel"
    }

    sealed class Event {
        data class Refresh(val force: Boolean = false) : Event()
        data class ShowDetail(val show: Boolean) : Event()
    }

    val poemSate = PoemState()

    init {
        viewModelScope.launch {
            val last =
                JsonUtils.fromJson<PoemInfo>(UserPrefManager.getString(UserPrefManager.Key.POEM_LAST))
            if (last != null) with(last) {
                poemSate.poemInfo = this
            }
        }
    }

    fun sendEvent(event: Event) {
        LLog.d(TAG, "sendEvent: $event")
        when (event) {
            is Event.Refresh -> refresh(event.force)
            is Event.ShowDetail -> poemSate.showDetail = event.show
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

    private val exceptionHandler = ExceptionHandler { exception ->
        poemSate.loading = false
    }

    private fun refresh(force: Boolean = false) {
        if (poemSate.loading) return

        if (!needRefresh(force)) return

        viewModelScope.launch(exceptionHandler) {
            poemSate.loading = true
            val poemResponse = withContext(Dispatchers.IO) {
                val token = getToken()
                poemService.getPoem(token)
            }
            poemSate.loading = false
            poemSate.poemInfo = PoemInfo(
                recommend = poemResponse.data.recommend,
                updateTime = System.currentTimeMillis(),
                origin = poemResponse.data.origin
            )
            UserPrefManager.putString(
                UserPrefManager.Key.POEM_LAST,
                JsonUtils.toJson(poemSate.poemInfo)
            )

            PoemAppWidgetProvider.tryUpdate()
        }
    }

    private fun needRefresh(force: Boolean): Boolean {
        if (force) return true

        val info = poemSate.poemInfo
        return info.recommend.isBlank() ||
                (System.currentTimeMillis() - info.updateTime > AppGlobalConfigs.poemUpdateInterval)
    }
}