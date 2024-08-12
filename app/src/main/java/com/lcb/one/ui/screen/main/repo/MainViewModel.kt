package com.lcb.one.ui.screen.main.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.prefs.UserPrefs
import com.lcb.one.ui.appwidget.PoemAppWidgetProvider
import com.lcb.one.ui.screen.main.repo.model.PoemInfo
import com.lcb.one.util.android.LLogger
import com.lcb.one.util.common.ExceptionHandler
import com.lcb.one.util.common.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    val poemInfo: MutableStateFlow<PoemInfo> = MutableStateFlow(PoemInfo())

    init {
        viewModelScope.launch {
            val last =
                JsonUtils.fromJsonOrDefault(
                    UserPrefs.get(UserPrefs.Key.lastPoem, ""),
                    PoemInfo.serializer()
                )
            LLogger.debug(TAG) { "init: last = $last" }
            if (last == null) {
                updatePoem(true)
            } else {
                poemInfo.update { last }
            }
        }
    }

    private val poemService = PoemApiService.instance

    private suspend fun getToken(): String {
        var token = UserPrefs.get(UserPrefs.Key.token, "")
        if (token.isNotEmpty()) return token
        token = poemService.getToken().token
        UserPrefs.put(UserPrefs.Key.token, token)

        return token
    }

    private var loading = false
    private val exceptionHandler = ExceptionHandler { exception ->
        loading = false
    }

    fun updatePoem(force: Boolean = false) {
        LLogger.debug(TAG) { "updatePoem: force = $force" }
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
                    author = poemResponse.data.origin.author,
                    dynasty = poemResponse.data.origin.dynasty,
                    title = poemResponse.data.origin.title,
                    content = poemResponse.data.origin.content
                )
            }
            UserPrefs
                .put(UserPrefs.Key.lastPoem, JsonUtils.toJson(poemInfo.value))
            PoemAppWidgetProvider.tryUpdate()
        }
    }

    private fun needRefresh(force: Boolean): Boolean {
        if (force) return true

        val info = poemInfo.value
        LLogger.debug(TAG) { "needRefresh: info = $info" }
        return info.recommend.isBlank() ||
                (System.currentTimeMillis() - info.updateTime > UserPrefs.poemUpdateInterval)
    }
}