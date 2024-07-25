package com.lcb.one.ui.screen.player.repo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val uri: Uri,
    val title: String,
    val artist: String,
    val duration: Long
) : Parcelable
