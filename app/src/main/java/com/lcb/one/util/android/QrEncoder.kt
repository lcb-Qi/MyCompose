package com.lcb.one.util.android

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.EnumMap

object QrEncoder {
    private val hints: EnumMap<EncodeHintType, Any> by lazy {
        EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8)
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            put(EncodeHintType.MARGIN, 0)
        }
    }
    private const val TAG = "QrEncoder"
    suspend fun createQrCode(
        content: String,
        size: Int,
        @ColorInt
        foregroundColor: Int = Color.BLACK,
        @ColorInt
        backgroundColor: Int = Color.WHITE,
        logo: Bitmap? = null
    ): Bitmap =
        withContext(Dispatchers.Default) {
            LLogger.debug(TAG) { "createQrCode: content = $content, size = $size" }

            val matrix = MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
            )
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (matrix[x, y]) {
                        pixels[y * size + x] = foregroundColor
                    } else {
                        pixels[y * size + x] = backgroundColor
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
                setPixels(pixels, 0, size, 0, 0, size, size)
            }

            return@withContext if (logo == null) bitmap else bitmap.addLogo(logo)
        }

    private fun Bitmap.addLogo(logo: Bitmap): Bitmap {
        val scaleFactor = width / 5f / logo.width
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val result = kotlin.runCatching {
            val paint = Paint().apply {
                isAntiAlias = true
                isFilterBitmap = true
                isDither = true
            }
            Canvas(bitmap).run {
                drawBitmap(this@addLogo, 0f, 0f, null)
                scale(scaleFactor, scaleFactor, width / 2f, height / 2f)
                drawBitmap(logo, (width - logo.width) / 2f, (height - logo.height) / 2f, paint)
                save()
                restore()
            }
        }.onFailure {
            LLogger.debug(TAG) { "addLogo failed: $it" }
        }

        return if (result.isFailure) {
            this
        } else {
            bitmap
        }
    }
}