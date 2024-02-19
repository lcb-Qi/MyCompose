package com.lcb.one.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val status: String,
    @Json(name = "data")
    val token: String
)