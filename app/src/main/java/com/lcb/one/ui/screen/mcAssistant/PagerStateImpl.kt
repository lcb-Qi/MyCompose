package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

// copy from androidx.compose.foundation.pager.PagerStateKt
@ExperimentalFoundationApi
class PagerStateImpl(
    initialPage: Int,
    initialPageOffsetFraction: Float,
    updatedPageCount: () -> Int
) : PagerState(initialPage, initialPageOffsetFraction) {

    var pageCountState = mutableStateOf(updatedPageCount)
    override val pageCount: Int get() = pageCountState.value.invoke()

    companion object {
        /**
         * To keep current page and current page offset saved
         */
        val Saver: Saver<PagerStateImpl, *> = listSaver(
            save = {
                listOf(
                    it.currentPage,
                    it.currentPageOffsetFraction,
                    it.pageCount
                )
            },
            restore = {
                PagerStateImpl(
                    initialPage = it[0] as Int,
                    initialPageOffsetFraction = it[1] as Float,
                    updatedPageCount = { it[2] as Int }
                )
            }
        )
    }
}

/**
 * copy from androidx.compose.foundation.pager.PagerStateKt#rememberPagerState
 * 官方没有提供传key和inputs的参数
 */
@ExperimentalFoundationApi
@Composable
fun rememberPagerState(
    vararg inputs: Any?,
    key: String? = null,
    initialPage: Int = 0,
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): PagerState {
    return rememberSaveable(inputs = inputs, key = key, saver = PagerStateImpl.Saver) {
        PagerStateImpl(
            initialPage,
            initialPageOffsetFraction,
            pageCount
        )
    }.apply {
        pageCountState.value = pageCount
    }
}