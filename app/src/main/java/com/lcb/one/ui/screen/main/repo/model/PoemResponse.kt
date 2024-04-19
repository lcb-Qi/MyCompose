package com.lcb.one.ui.screen.main.repo.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PoemResponse(
    @SerialName("data")
    val `data`: Data = Data(),

    @SerialName("ipAddress")
    val ipAddress: String = "", // 183.45.78.113

    @SerialName("status")
    val status: String = "", // success

    @SerialName("token")
    val token: String = "", // 5k9qhf7ZYSrfG9p5L3pARfPUjzvBa+6V

    @SerialName("warning")
    @Transient
    val warning: Any? = null // null
) {
    @Serializable
    data class Data(
        @SerialName("cacheAt")
        val cacheAt: String = "", // 2024-01-19T15:47:23.271504009

        @SerialName("content")
        val recommend: String = "", // 拂拂风前度暗香，月色侵花冷。

        @SerialName("id")
        val id: String = "", // 5b8b9572e116fb3714e72601

        @SerialName("matchTags")
        val matchTags: List<String> = listOf(),

        @SerialName("origin")
        val origin: Origin = Origin(),

        @SerialName("popularity")
        val popularity: Int = 0, // 2130

        @SerialName("recommendedReason")
        val recommendedReason: String = ""
    ) {
        @Serializable
        data class Origin(
            @SerialName("author")
            val author: String = "", // 朱淑真

            @SerialName("content")
            val content: List<String> = listOf(),

            @SerialName("dynasty")
            val dynasty: String = "", // 宋代

            @SerialName("title")
            val title: String = "", // 卜算子（咏梅）

            @SerialName("translate")
            val translate: List<String>? = null // null
        )
    }
}