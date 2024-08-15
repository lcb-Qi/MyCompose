package com.lcb.one.util.common

import com.lcb.one.BuildConfig
import com.lcb.one.util.android.LLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ExceptionHandler(val onException: (Throwable) -> Unit = {}) : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        LLogger.warn("CoroutineExceptionHandler") { "catch a exception: $exception" }
        if (BuildConfig.DEBUG) {
            exception.printStackTrace()
        }

        onException(exception)
    }
}

suspend fun <T> withIoSafely(block: suspend CoroutineScope.() -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        kotlin.runCatching { block() }
    }
}