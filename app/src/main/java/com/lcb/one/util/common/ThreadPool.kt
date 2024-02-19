package com.lcb.one.util.common

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

object ThreadPool {
    private val DEFAULT_FACTORY: ThreadFactory = object : ThreadFactory {
        private val count = AtomicInteger(1)
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "aTool-Thread#" + count.getAndIncrement())
        }
    }

    private val defaultPool by lazy {
        ThreadPoolExecutor(
            5,
            10,
            180, TimeUnit.SECONDS,
            ArrayBlockingQueue(10),
            DEFAULT_FACTORY
        )
    }

    private val scheduledPool by lazy {
        ScheduledThreadPoolExecutor(5, DEFAULT_FACTORY)
    }

    fun executeOnBackground(task: Runnable) {
        defaultPool.execute(task)
    }

    fun submitToBackground(task: Runnable) {
        defaultPool.submit(task)
    }

    fun schedule(task: Runnable, mills: Long) {
        scheduledPool.schedule(task, mills, TimeUnit.MILLISECONDS)
    }
}