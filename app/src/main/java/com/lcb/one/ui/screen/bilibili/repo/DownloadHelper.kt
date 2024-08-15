package com.lcb.one.ui.screen.bilibili.repo

import com.lcb.one.util.android.LLogger
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.getRelativePath
import com.lcb.one.util.android.outputStream
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.common.withIoSafely
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.DecimalFormat

typealias ProgressListener = (downloadedBytes: Long, totalBytes: Long) -> Unit

object DownloadHelper {
    private const val TAG = "DownloadHelper"
    private val downloadClient by lazy { OkHttpClient.Builder().build() }
    private const val BUFFER_SIZE = 1024 * 8
    private const val NOTIFY_INTERVAL = 1000

    private const val KB = 1024f
    private const val MB = 1024f * KB
    private const val GB = 1024f * MB

    fun friendlySize(bytes: Long): String {
        if (bytes == 0L) {
            return "0 B"
        }

        return with(DecimalFormat("#.00")) {
            when {
                bytes < KB -> format(bytes) + " B"
                bytes < MB -> format(bytes / KB) + " KiB"
                bytes < GB -> format(bytes / MB) + " MiB"
                else -> format(bytes / GB) + " GiB"
            }
        }
    }

    suspend fun downloadFile(
        url: String,
        filename: String = DateTimeUtils.nowStringShort(),
        listener: ProgressListener? = null
    ): Result<String> {
        LLogger.debug(TAG) { "downloadImage: $url" }

        return withIoSafely {
            val request = Request.Builder()
                .url(url)
                .build()

            val res = downloadClient.newCall(request).execute()

            require(res.isSuccessful && res.body != null) { "downloadImage failed" }

            val localUri = StorageUtils.createUri(res.body!!.contentType().toString(), filename)
            localUri!!.outputStream()!!.use { output ->
                val totalBytes = res.body!!.contentLength()
                res.body!!.byteStream().use { input ->
                    val buffer = ByteArray(BUFFER_SIZE)
                    var copyBytes = 0L
                    var lastNotifyTime = 0L
                    var readCount = input.read(buffer)
                    while (readCount >= 0) {
                        output.write(buffer, 0, readCount)
                        copyBytes += readCount
                        readCount = input.read(buffer)

                        if (listener == null) continue

                        if (copyBytes == totalBytes || System.currentTimeMillis() - lastNotifyTime > NOTIFY_INTERVAL) {
                            lastNotifyTime = System.currentTimeMillis()
                            listener.invoke(copyBytes, totalBytes)
                        }
                    }
                }
            }

            localUri.getRelativePath()
        }
    }
}

