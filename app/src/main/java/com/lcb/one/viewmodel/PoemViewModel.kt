package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.bean.PoemResponse
import com.lcb.one.network.PoemServerAccessor
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.page.AppSettings
import com.lcb.one.util.common.JsonUtils
import com.lcb.one.util.android.SharedPrefUtils
import com.lcb.one.util.common.launchOnIO
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.MutableStateFlow

class PoemViewModel : ViewModel() {
    companion object {
        private const val TAG = "PoemViewModel"
        var durationIndex = AppSettings.getPoemUpdateDurationIndex(MyApp.getAppContext())
        var duration = AppSettings.getPoemUpdateDuration(MyApp.getAppContext(), durationIndex)
        private const val KEY_POEM_TOKEN = "poem_token"
        private const val KEY_LAST_POEM = "last_poem"
    }

    private val poemApiService = PoemServerAccessor.getService()

    private suspend fun getToken(): String {
        var token = SharedPrefUtils.getString(KEY_POEM_TOKEN)
        if (token.isNotEmpty()) return token

        token = poemApiService.getToken().token
        SharedPrefUtils.putString(KEY_POEM_TOKEN, token)

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
            val fromJson = JsonUtils.fromJson<PoemInfo>(SharedPrefUtils.getString(KEY_LAST_POEM))
            recommend = fromJson?.recommend ?: ""
            updateTime = fromJson?.updateTime ?: -1
            origin = fromJson?.origin ?: PoemResponse.Data.Origin()
        })
    }

    val isLoading = MutableStateFlow(false)

    fun getPoem(forceRefresh: Boolean = false) {
        if (!needRefresh(forceRefresh)) return

        viewModelScope.launchOnIO {
            isLoading.value = true
            refreshPoem()
            isLoading.value = false
        }
    }

    private fun needRefresh(forceRefresh: Boolean): Boolean {
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
        SharedPrefUtils.putString(KEY_LAST_POEM, JsonUtils.toJson(poemFlow.value))
    }
}