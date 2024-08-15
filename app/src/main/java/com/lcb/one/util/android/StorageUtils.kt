package com.lcb.one.util.android

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.lcb.one.ui.MyApp
import com.lcb.one.util.common.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.io.File

object MimeType {
    const val IMAGE_PNG = "image/png"
    const val APPLICATION_PACKAGE = "application/vnd.android.package-archive"
    const val TEXT_DOCUMENT = "text/plain"
}

object StorageUtils {
    private const val TAG = "StorageUtils"
    private val DEFAULT_DIR = Environment.DIRECTORY_DOWNLOADS
    private const val DEFAULT_IMAGE_SUB_DIR = "SaltFish"
    private val DEFAULT_RELATIVE_PATH = "$DEFAULT_DIR/$DEFAULT_IMAGE_SUB_DIR/"

    fun createUri(
        mimeType: String,
        filename: String = DateTimeUtils.nowStringShort(),
        relativePath: String = DEFAULT_RELATIVE_PATH
    ): Uri? {
        LLogger.debug(TAG) { "insertToStorage: mimeType = $mimeType, filename = $filename, relativePath = $relativePath" }

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }

        return MyApp.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    }

    private inline fun <reified T> failureOfCreatingFile() =
        Result.failure<T>(RuntimeException("Failed to create uri in storage."))

    // public api start
    suspend fun createImageFile(bitmap: Bitmap, fileName: String) = withContext(Dispatchers.IO) {
        LLogger.debug(TAG) { "createImageFile: " }

        val uri = createUri(MimeType.IMAGE_PNG, fileName)
        val result = uri?.outputStream()?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            bitmap.recycle()

            Result.success(uri.getRelativePath())
        } ?: failureOfCreatingFile()

        return@withContext result
    }

    suspend fun saveApk(srcPath: String, targetFileName: String) = withContext(Dispatchers.IO) {
        LLogger.debug(TAG) { "saveApk: $srcPath" }

        val uri = createUri(MimeType.APPLICATION_PACKAGE, targetFileName)
        val result = uri?.bufferedSink()?.use { sink ->
            File(srcPath).source().buffer().use { source ->
                sink.writeAll(source)
            }

            Result.success(uri.getRelativePath())
        } ?: failureOfCreatingFile()

        return@withContext result
    }

    suspend fun createDocument(text: String, filename: String) = withContext(Dispatchers.IO) {
        LLogger.debug(TAG) { "createDocument: " }

        val uri = createUri(MimeType.TEXT_DOCUMENT, filename)
        val result = uri?.bufferedSink()?.use { sink ->
            sink.writeUtf8(text)

            Result.success(uri.getRelativePath())
        } ?: failureOfCreatingFile()


        return@withContext result
    }
}