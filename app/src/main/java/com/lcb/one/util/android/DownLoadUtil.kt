package com.lcb.one.util.android

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import com.lcb.one.network.commonApiService
import com.lcb.one.network.core.OkhttpFactory
import com.lcb.one.ui.MyApp
import com.lcb.one.util.common.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody

object DownLoadUtil {
    private const val TAG = "DownLoadUtil"

    private val DEFAULT_IMAGE_RELATIVE_PATH = Environment.DIRECTORY_PICTURES
    private const val DEFAULT_IMAGE_SUB_DIR = "SaltFish"


    suspend fun saveImageFromUrl(url: String, fileName: String = DateTimeUtils.nowStringShort()) =
        withContext(Dispatchers.IO) {
            if (url.isBlank() || fileName.isBlank()) return@withContext
            val responseBody = commonApiService.downloadFile(url)
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
                val relativePath = "$DEFAULT_IMAGE_RELATIVE_PATH/$DEFAULT_IMAGE_SUB_DIR/"
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