package com.lcb.one.util.android

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.lcb.one.network.CommonApiService
import com.lcb.one.ui.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.InputStream

object StorageUtils {
    private const val TAG = "StorageUtils"

    private val DEFAULT_DIR = Environment.DIRECTORY_DOWNLOADS
    private const val DEFAULT_IMAGE_SUB_DIR = "SaltFish"

    private fun isStorageAvailable() =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun checkStorageAvailable() {
        if (!isStorageAvailable()) throw RuntimeException("Storage not mounted.")
    }

    private fun Uri.outputStream() = MyApp.getContentResolver().openOutputStream(this)

    private fun Uri.bufferedSink() = outputStream()?.sink()?.buffer()

    private fun insertToStorage(mimeType: String, fileName: String): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, getRelativePathFromMimeType(mimeType))
        }

        return MyApp.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    }

    private fun getRelativePathFromMimeType(mimeType: String): String {
        // 目前都放在Download下
        return "$DEFAULT_DIR/$DEFAULT_IMAGE_SUB_DIR/"
    }

    private inline fun <reified T> failureOfCreatingFile() =
        Result.failure<T>(RuntimeException("Failed to create uri in storage."))

    // public api start

    suspend fun createImageFromUrl(url: String, filename: String) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "createImageFromUrl: $url")

        require(url.isNotBlank() && filename.isNotBlank()) {
            "url and filename must not empty."
        }

        val responseBody = CommonApiService.instance.downloadFile(url)
        val result = responseBody.use {
            createImageFile(it.byteStream(), filename, it.contentType().toString())
        }

        result
    }

    suspend fun createImageFile(
        input: InputStream,
        fileName: String,
        contentType: String = "image/jpeg"
    ) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "createImageFile: ")

        val uri = insertToStorage(contentType, fileName)
        val result = uri?.outputStream()?.use {
            input.copyTo(it)

            Result.success(uri.toRelativePath())
        } ?: failureOfCreatingFile()

        return@withContext result
    }

    suspend fun createImageFile(bitmap: Bitmap, fileName: String) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "createImageFile: ")

        val uri = insertToStorage("image/jpeg", fileName)
        val result = uri?.outputStream()?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            bitmap.recycle()

            Result.success(uri.toRelativePath())
        } ?: failureOfCreatingFile()

        return@withContext result
    }

    suspend fun saveApk(srcPath: String, targetFileName: String) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "saveApk: $srcPath")

        val mimeType = "application/vnd.android.package-archive"
        val uri = insertToStorage(mimeType, targetFileName)
        val result = uri?.bufferedSink()?.use { sink ->
            File(srcPath).source().use { source ->
                sink.writeAll(source)
            }

            Result.success(uri.toRelativePath())
        } ?: failureOfCreatingFile()

        return@withContext result
    }

    suspend fun createDocument(text: String, filename: String) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "saveToFile: ")

        val uri = insertToStorage("text/plain", filename)
        val result = uri?.bufferedSink()?.use { sink ->
            sink.writeUtf8(text)

            Result.success(uri.toRelativePath())
        } ?: failureOfCreatingFile()


        return@withContext result
    }
}