package com.lcb.one.ui.screen.player.repo

sealed interface ControllerEvent {
    data object ChangeRepeatMode : ControllerEvent
    data object PlayOrPause : ControllerEvent
    data object Next : ControllerEvent
    data object Previous : ControllerEvent
    data class SeekTo(val index: Int, val position: Long = 0) : ControllerEvent
    data class SeekToPosition(val position: Long) : ControllerEvent
}