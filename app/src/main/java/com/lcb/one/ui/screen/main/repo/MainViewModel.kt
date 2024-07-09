package com.lcb.one.ui.screen.main.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.screen.main.repo.model.PoemInfo
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.UserPref
import com.lcb.one.util.common.ExceptionHandler
import com.lcb.one.util.common.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "PoemViewModel"
    }

    val poemInfo: MutableStateFlow<PoemInfo> = MutableStateFlow(PoemInfo())

    init {
        viewModelScope.launch {
            val last =
                JsonUtils.fromJsonOrDefault(
                    UserPref.getString(UserPref.Key.POEM_LAST),
                    PoemInfo.serializer()
                )
            poemInfo.update { last ?: it }
        }
    }

    private val poemService = PoemApiService.instance

    private suspend fun getToken(): String {
        var token = UserPref.getString(UserPref.Key.POEM_TOKEN)
        if (token.isNotEmpty()) return token
        token = poemService.getToken().token
        UserPref.putString(UserPref.Key.POEM_TOKEN, token)

        return token
    }

    private var loading = false
    private val exceptionHandler = ExceptionHandler { exception ->
        loading = false
    }

    fun updatePoem(force: Boolean = false) {
        LLog.d(TAG, "updatePoem: force = $force")
        if (loading) return

        if (!needRefresh(force)) return

        viewModelScope.launch(exceptionHandler) {
            loading = true
            val poemResponse = withContext(Dispatchers.IO) {
                val token = getToken()
                poemService.getPoem(token)
            }
            loading = false
            poemInfo.update {
                PoemInfo(
                    recommend = poemResponse.data.recommend,
                    updateTime = System.currentTimeMillis(),
                    origin = poemResponse.data.origin
                )
            }
            UserPref.putString(UserPref.Key.POEM_LAST, JsonUtils.toJson(poemInfo.value))
            PoemAppWidgetProvider.tryUpdate()
        }
    }

    private fun needRefresh(force: Boolean): Boolean {
        if (force) return true

        val info = poemInfo.value
        return info.recommend.isBlank() ||
                (System.currentTimeMillis() - info.updateTime > AppGlobalConfigs.poemUpdateInterval)
    }
}