package com.lcb.one.ui.screen.main.repo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lcb.one.ui.screen.main.repo.model.PoemInfo

class PoemState {
    var poemInfo by mutableStateOf(PoemInfo())

    var showDetail by mutableStateOf(false)
}