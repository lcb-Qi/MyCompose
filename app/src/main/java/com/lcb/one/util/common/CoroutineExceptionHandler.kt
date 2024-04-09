package com.lcb.one.util.common

import com.lcb.one.util.android.LLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private const val TAG = "CoroutineExceptionHandler"
private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    LLog.d(TAG, "catch a exception: $throwable")
    if (throwable is IOException) {
        LLog.d(TAG, "ignore IOException")
    } else {
        throw throwable
    }
}

fun CoroutineScope.launchSafely(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context + exceptionHandler, start, block)