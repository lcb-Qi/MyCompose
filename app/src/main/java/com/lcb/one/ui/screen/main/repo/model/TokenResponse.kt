package com.lcb.one.ui.screen.main.repo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("status")
    val status: String,

    @SerialName("data")
    val token: String
)