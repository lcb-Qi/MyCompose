package com.lcb.one.util

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import com.lcb.one.ui.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

object DownLoadUtil {
    private const val TAG = "DownLoadUtil"

    private val defaultImageRelativePath = Environment.DIRECTORY_PICTURES
    private val defaultImageDir =
        Environment.getExternalStoragePublicDirectory(defaultImageRelativePath)
    private const val defaultImageSubDir = "aTool"

    interface DownloadService {
        @GET
        @Streaming
        suspend fun downloadFromUrl(@Url url: String): ResponseBody
    }

    private val downloadService by lazy {
        Retrofit.Builder()
            .baseUrl("https://127.0.0.1/")
            .build()
            .create(DownloadService::class.java)
    }

    suspend fun saveImageFromUrl(url: String, fileName: String = DateTimeUtils.nowStringShort()) =
        withContext(Dispatchers.IO) {
            if (url.isBlank() || fileName.isBlank()) return@withContext
            val responseBody = downloadService.downloadFromUrl(url)
            responseBody.writeToImageFile(fileName)
        }


    private fun ResponseBody.writeToImageFile(fileName: String) = use {
        if (!contentType().isImage()) return@use

        val bitmap = BitmapFactory.decodeStream(it.byteStream())
        writeBitmapToImageFile(bitmap, fileName, it.contentType().toString())
    }

    fun writeBitmapToImageFile(
        bitmap: Bitmap,
        fileName: String = DateTimeUtils.nowStringShort(),
        contentType: String = "image/jpeg"
    ) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val values = ContentValues().apply {
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
                put(MediaStore.Images.ImageColumns.MIME_TYPE, contentType)
                val relativePath = "${defaultImageRelativePath}/${defaultImageSubDir}/"
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativePath)
            }

            val resolver = MyApp.getAppContext().contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { os ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    bitmap.recycle()
                }
                ToastUtils.showToast("保存成功 ${uri.toAbsolutePath(MyApp.getAppContext())}")
            } else {
                ToastUtils.showToast("保存失败")
            }
        } else {
            ToastUtils.showToast("保存失败")
        }
    }

    private fun MediaType?.isImage(): Boolean {
        return this != null && type == "image"
    }
}