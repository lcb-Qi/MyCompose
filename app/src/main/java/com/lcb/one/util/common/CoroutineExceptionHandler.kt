package com.lcb.one.util.common

import com.lcb.one.BuildConfig
import com.lcb.one.util.android.LLogger
import kotlinx.coroutines.CoroutineExceptionHandler
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