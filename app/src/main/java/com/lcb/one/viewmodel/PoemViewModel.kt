package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.bean.PoemResponse
import com.lcb.one.network.PoemServerAccessor
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.page.AppSettings
import com.lcb.one.util.JsonUtils
import com.lcb.one.util.LLog
import com.lcb.one.util.SharedPrefUtils
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PoemViewModel : ViewModel() {
    companion object {
        private const val TAG = "PoemViewModel"
        var durationIndex = AppSettings.getPoemUpdateDurationIndex(MyApp.getAppContext())
        var duration = AppSettings.getPoemUpdateDuration(MyApp.getAppContext(), durationIndex)
    }

    private val poemApiService = PoemServerAccessor.getService()

    private suspend fun getToken(): String {
        var token = SharedPrefUtils.getToken()
        if (token.isNotEmpty()) return token

        token = poemApiService.getToken().token
        SharedPrefUtils.saveToken(token)

        return token
    }

    @JsonClass(generateAdapter = true)
    data class PoemInfo(
        var recommend: String = "",
        var updateTime: Long = -1,
        var origin: PoemResponse.Data.Origin = PoemResponse.Data.Origin()
    )

    val poemFlow by lazy {
        MutableStateFlow(PoemInfo().apply {
            val fromJson = JsonUtils.fromJson<PoemInfo>(SharedPrefUtils.getPoem())
            recommend = fromJson?.recommend ?: ""
            updateTime = fromJson?.updateTime ?: -1
            origin = fromJson?.origin ?: PoemResponse.Data.Origin()
        })
    }

    val isLoading = MutableStateFlow(false)

    fun getPoem(forceRefresh: Boolean = false) {
        if (!needRefresh(forceRefresh)) return

        viewModelScope.launch {
            isLoading.value = true
            refreshPoem()
            isLoading.value = false
        }
    }

    private fun needRefresh(forceRefresh: Boolean): Boolean {
        LLog.d(TAG, "needRefresh: duration = $duration")
        return poemFlow.value.recommend.isBlank() || forceRefresh || System.currentTimeMillis() - poemFlow.value.updateTime > duration
    }

    private suspend fun refreshPoem() {
        val poemResponse = poemApiService.getPoem(getToken())
        val info = PoemInfo().apply {
            recommend = poemResponse.data.content
            updateTime = System.currentTimeMillis()
            origin = poemResponse.data.origin
        }
        poemFlow.value = info
        SharedPrefUtils.savePoem(JsonUtils.toJson(poemFlow.value))
    }
}