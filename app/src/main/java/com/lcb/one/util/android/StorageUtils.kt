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
import java.io.File
import java.io.InputStream

object StorageUtils {
    private const val TAG = "StorageUtils"

    private val DEFAULT_DIR = Environment.DIRECTORY_DOWNLOADS
    private const val DEFAULT_IMAGE_SUB_DIR = "SaltFish"

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
        val result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val uri = insertToStorage(contentType, fileName)
            if (uri != null) {
                MyApp.getContentResolver().openOutputStream(uri)?.use {
                    input.copyTo(it)
                }
                Result.success(uri.toRelativePath(MyApp.get()))
            } else {
                Result.failure(RuntimeException("Failed to create uri in storage."))
            }
        } else {
            Result.failure(RuntimeException("Failed to access storage."))
        }

        return@withContext result
    }

    suspend fun createImageFile(
        bitmap: Bitmap,
        fileName: String,
        contentType: String = "image/jpeg"
    ) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "createImageFile: ")
        val result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val uri = insertToStorage(contentType, fileName)
            if (uri != null) {
                MyApp.getContentResolver().openOutputStream(uri)?.use { os ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    bitmap.recycle()
                }
                Result.success(uri.toRelativePath(MyApp.get()))
            } else {
                Result.failure(RuntimeException("Failed to create uri in storage."))
            }
        } else {
            Result.failure(RuntimeException("Failed to access storage."))
        }

        return@withContext result
    }

    suspend fun saveApk(apkPath: String, fileName: String) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "saveApk: $apkPath")

        val mimeType = "application/vnd.android.package-archive"
        val result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val uri = insertToStorage(mimeType, fileName)
            if (uri != null) {
                MyApp.getContentResolver().openOutputStream(uri)?.use { os ->
                    File(apkPath).inputStream().copyTo(os)
                }
                Result.success(uri.toRelativePath(MyApp.get()))
            } else {
                Result.failure(RuntimeException("Failed to create uri in storage."))
            }
        } else {
            Result.failure(RuntimeException("Failed to access storage."))
        }

        return@withContext result
    }

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

    suspend fun createDocument(text: String, filename: String) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "saveToFile: ")
        val result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val uri = insertToStorage("text/plain", filename)
            if (uri != null) {
                MyApp.getContentResolver().openOutputStream(uri)?.use {
                    it.bufferedWriter().use { writer ->
                        writer.write(text)
                    }
                }
                Result.success(uri.toRelativePath(MyApp.get()))
            } else {
                Result.failure(RuntimeException("Failed to create uri in storage."))
            }
        } else {
            Result.failure(RuntimeException("Failed to access storage."))
        }

        return@withContext result
    }
}