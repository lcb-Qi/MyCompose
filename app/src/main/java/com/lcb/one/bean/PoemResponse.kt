package com.lcb.one.bean


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PoemResponse(
    @Json(name = "data")
    val `data`: Data = Data(),
    @Json(name = "ipAddress")
    val ipAddress: String = "", // 183.45.78.113
    @Json(name = "status")
    val status: String = "", // success
    @Json(name = "token")
    val token: String = "", // 5k9qhf7ZYSrfG9p5L3pARfPUjzvBa+6V
    @Json(name = "warning")
    val warning: Any? = null // null
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "cacheAt")
        val cacheAt: String = "", // 2024-01-19T15:47:23.271504009
        @Json(name = "content")
        val content: String = "", // 拂拂风前度暗香，月色侵花冷。
        @Json(name = "id")
        val id: String = "", // 5b8b9572e116fb3714e72601
        @Json(name = "matchTags")
        val matchTags: List<String> = listOf(),
        @Json(name = "origin")
        val origin: Origin = Origin(),
        @Json(name = "popularity")
        val popularity: Int = 0, // 2130
        @Json(name = "recommendedReason")
        val recommendedReason: String = ""
    ) {
        @JsonClass(generateAdapter = true)
        data class Origin(
            @Json(name = "author")
            val author: String = "", // 朱淑真
            @Json(name = "content")
            val content: List<String> = listOf(),
            @Json(name = "dynasty")
            val dynasty: String = "", // 宋代
            @Json(name = "title")
            val title: String = "", // 卜算子（咏梅）
            @Json(name = "translate")
            val translate: Any? = null // null
        )
    }
}