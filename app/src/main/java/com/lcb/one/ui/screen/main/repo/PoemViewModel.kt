package com.lcb.one.ui.screen.main.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.screen.main.repo.model.PoemResponse
import com.lcb.one.util.android.SharedPrefUtils
import com.lcb.one.util.common.JsonUtils
import com.lcb.one.util.common.launchSafely
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class PoemViewModel : ViewModel() {
    companion object {
        const val KEY_POEM_TOKEN = "poem_token"
        const val KEY_LAST_POEM = "last_poem"
    }

    private val poemService = PoemApiService.instance

    private suspend fun getToken(): String {
        var token = SharedPrefUtils.getString(KEY_POEM_TOKEN)
        if (token.isNotEmpty()) return token
        token = poemService.getToken().token
        SharedPrefUtils.putString(KEY_POEM_TOKEN, token)

        return token
    }

    @Serializable
    data class PoemInfo(
        val recommend: String = "",
        val updateTime: Long = -1,
        val origin: PoemResponse.Data.Origin = PoemResponse.Data.Origin()
    )

    val poemFlow: MutableStateFlow<PoemInfo> by lazy {
        val last = SharedPrefUtils.getString(KEY_LAST_POEM)
        MutableStateFlow(JsonUtils.fromJson(last) ?: PoemInfo())
    }

    val isLoading = MutableStateFlow(false)

    fun refresh(forceRefresh: Boolean = false) {
        if (!needRefresh(forceRefresh)) return

        viewModelScope.launchSafely {
            isLoading.value = true
            val poemResponse = withContext(Dispatchers.IO) {
                poemService.getPoem(getToken())
            }
            poemFlow.value =
                PoemInfo(
                    recommend = poemResponse.data.content,
                    updateTime = System.currentTimeMillis(),
                    origin = poemResponse.data.origin
                )
            SharedPrefUtils.putString(KEY_LAST_POEM, JsonUtils.toJson(poemFlow.value))
            isLoading.value = false
        }
    }

    private fun needRefresh(forceRefresh: Boolean): Boolean {
        val info = poemFlow.value
        return info.recommend.isBlank() ||
                forceRefresh ||
                (System.currentTimeMillis() - info.updateTime > AppGlobalConfigs.poemUpdateDuration)
    }
}