package com.lcb.one.util.common

import com.lcb.one.util.android.LLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
    LLog.w("CoroutineExceptionHandler", "exception: ${throwable}")
}

fun <T> CoroutineScope.launchOnIO(block: suspend CoroutineScope.() -> T): Job =
    launch(Dispatchers.IO + exceptionHandler) {
        block()
    }

