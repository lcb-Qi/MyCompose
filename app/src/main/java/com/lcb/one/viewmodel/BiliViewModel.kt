package com.lcb.one.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcb.one.network.BiliServerAccessor
import com.lcb.one.util.android.LLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BiliViewModel : ViewModel() {
    private val TAG = "BiliViewModel"
    val coverUrl = MutableStateFlow("")

    val isLoading = MutableStateFlow(false)

    fun getCoverUrl(videoId: String) {
        LLog.d(TAG, "getCoverUrl: videoId = $videoId")
        viewModelScope.launch {
            isLoading.value = true
            BiliServerAccessor.getVideoInfo(videoId)?.let {
                LLog.d(TAG, "getCoverUrl: it.data.pic = ${it.data.pic}")
                coverUrl.value = it.data.pic
            }
            isLoading.value = false
        }
    }

    companion object {

        private const val TABLE = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF"
        // private val mp = HashMap<String, Int>()
        // private val mp2 = HashMap<Int, String>()
        private val ss = intArrayOf(11, 10, 3, 8, 4, 6, 2, 9, 5, 7)
        private const val XOR: Long = 177451812
        private const val ADD = 8728348608L
    }


    fun Int.power(b: Int): Long {
        var power: Long = 1
        for (c in 0 until b) power *= this.toLong()
        return power
    }

    fun bv2av(bvId: String): String {
        val mp = HashMap<String, Int>()
        var r: Long = 0
        for (i in 0..57) {
            val s1 = TABLE.substring(i, i + 1)
            mp[s1] = i
        }
        for (i in 0..5) {
            r += mp[bvId.substring(ss[i], ss[i] + 1)]!! * 58.power(i)
        }
        return "av" + ((r - ADD) xor XOR)
    }

    fun av2bv(avId: String): String {
        val mp2 = HashMap<Int, String>()
        var s = avId.split("av".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toLong()
        val sb = StringBuffer("BV1  4 1 7  ")
        s = (s xor XOR) + ADD
        for (i in 0..57) {
            val s1 = TABLE.substring(i, i + 1)
            mp2[i] = s1
        }
        for (i in 0..5) {
            val r = mp2[(s / 58.power(i) % 58).toInt()]
            sb.replace(ss[i], ss[i] + 1, r)
        }
        return sb.toString()
    }
}