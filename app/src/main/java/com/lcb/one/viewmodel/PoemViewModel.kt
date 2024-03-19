package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.bean.PoemResponse
import com.lcb.one.network.PoemServerAccessor
import com.lcb.one.ui.AppSettings
import com.lcb.one.util.common.JsonUtils
import com.lcb.one.util.android.SharedPrefUtils
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PoemViewModel : ViewModel() {
    companion object {
        const val KEY_POEM_TOKEN = "poem_token"
        const val KEY_LAST_POEM = "last_poem"
    }


    private suspend fun getToken(): String {
        var token = SharedPrefUtils.getString(KEY_POEM_TOKEN)
        if (token.isNotEmpty()) return token

        token = PoemServerAccessor.getToken()?.token ?: ""
        SharedPrefUtils.putString(KEY_POEM_TOKEN, token)

        return token
    }

    @JsonClass(generateAdapter = true)
    data class PoemInfo(
        val recommend: String = "",
        val updateTime: Long = -1,
        val origin: PoemResponse.Data.Origin = PoemResponse.Data.Origin()
    )

    val poemFlow by lazy {
        MutableStateFlow(
            JsonUtils.fromJson<PoemInfo>(SharedPrefUtils.getString(KEY_LAST_POEM)) ?: PoemInfo()
        )
    }

    val isLoading = MutableStateFlow(false)

    fun refresh(forceRefresh: Boolean = false) {
        if (!needRefresh(forceRefresh)) return

        viewModelScope.launch {
            isLoading.value = true
            PoemServerAccessor.getPoem(getToken())?.let {
                poemFlow.value =
                    PoemInfo(it.data.content, System.currentTimeMillis(), it.data.origin)
                SharedPrefUtils.putString(KEY_LAST_POEM, JsonUtils.toJson(poemFlow.value))
            }
            isLoading.value = false
        }
    }

    private fun needRefresh(forceRefresh: Boolean): Boolean {
        val info = poemFlow.value
        return info.recommend.isBlank() || forceRefresh || System.currentTimeMillis() - info.updateTime > AppSettings.poemUpdateDuration
    }
}