package com.lcb.one.util.android

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.lcb.one.network.CommonApiService
import com.lcb.one.ui.MyApp
import com.lcb.one.util.common.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object DownLoadUtil {
    private const val TAG = "DownLoadUtil"

    private val DEFAULT_DIR = Environment.DIRECTORY_DOWNLOADS
    private const val DEFAULT_IMAGE_SUB_DIR = "SaltFish"

    suspend fun saveImageFromUrl(
        url: String,
        filename: String = DateTimeUtils.nowStringShort(),
    ): Result<String> =
        withContext(Dispatchers.IO) {
            LLog.d(TAG, "saveImageFromUrl: ")

            require(url.isNotBlank() && filename.isNotBlank()) {
                "url and filename must not empty."
            }

            val responseBody = CommonApiService.instance.downloadFile(url)
            val result = responseBody.use {
                val bitmap = BitmapFactory.decodeStream(it.byteStream())
                writeBitmapToImageFile(bitmap, filename, it.contentType().toString())
            }

            result
        }

    suspend fun writeBitmapToImageFile(
        bitmap: Bitmap,
        fileName: String = DateTimeUtils.nowStringShort(),
        contentType: String = "image/jpeg",
    ) = withContext(Dispatchers.IO) {
        LLog.d(TAG, "writeBitmapToImageFile: ")
        val result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val uri = insertToStorage(contentType, fileName)
            if (uri != null) {
                MyApp.getAppContext().contentResolver.openOutputStream(uri)?.use { os ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    bitmap.recycle()
                }
                Result.success(uri.toRelativePath(MyApp.getAppContext()))
            } else {
                Result.failure(RuntimeException("Failed to create uri in storage."))
            }
        } else {
            Result.failure(RuntimeException("Failed to access storage."))
        }

        return@withContext result
    }

    fun saveApk(
        apkPath: String,
        fileName: String = DateTimeUtils.nowStringShort(),
    ): Result<String> {
        LLog.d(TAG, "saveApk: apkPath = $apkPath")

        val mimeType = "application/vnd.android.package-archive"
        val result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val uri = insertToStorage(mimeType, fileName)
            if (uri != null) {
                MyApp.getAppContext().contentResolver.openOutputStream(uri)?.use { os ->
                    File(apkPath).inputStream().copyTo(os)
                }
                Result.success(uri.toRelativePath(MyApp.getAppContext()))
            } else {
                Result.failure(RuntimeException("Failed to create uri in storage."))
            }
        } else {
            Result.failure(RuntimeException("Failed to access storage."))
        }

        return result
    }

    private fun insertToStorage(mimeType: String, fileName: String): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, getRelativePathFromMimeType(mimeType))
        }

        return MyApp.getAppContext().contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        )
    }

    private fun getRelativePathFromMimeType(mimeType: String): String {
        // 目前都放在Download下
        return when (mimeType) {
            "application/vnd.android.package-archive" -> "$DEFAULT_DIR/$DEFAULT_IMAGE_SUB_DIR/"
            "image/jpeg", "image/jpg", "image/png" -> "$DEFAULT_DIR/$DEFAULT_IMAGE_SUB_DIR/"
            else -> "$DEFAULT_DIR/$DEFAULT_IMAGE_SUB_DIR/"
        }
    }
}