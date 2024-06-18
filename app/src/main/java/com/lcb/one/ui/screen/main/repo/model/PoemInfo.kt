package com.lcb.one.ui.screen.main.repo.model

import kotlinx.serialization.Serializable

@Serializable
data class PoemInfo(
    val recommend: String = "",
    val updateTime: Long = -1,
    val origin: PoemResponse.Data.Origin = PoemResponse.Data.Origin()
)