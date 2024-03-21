package com.lcb.one.ui.service

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.lcb.one.util.android.LLog
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownloadInfo(val url: String, val filename: String) : Parcelable

enum class DownLoadState {
    IDLE, DOWNLOADING, SUCCESS, FAILED
}


class DownLoadService : LifecycleService() {
    companion object {
        private const val TAG = "DownLoadService"
    }

    private val downloadManager by lazy { getSystemService(DownloadManager::class.java) }
    private var downloadId: Long? = null

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            LLog.d("DownLoadService", "onReceive: $action")
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId != id) return
                val uri = downloadManager.getUriForDownloadedFile(downloadId!!)
                if (uri == null) {
                    DownLoadState.FAILED
                    listener?.invoke(DownLoadState.FAILED, null)
                } else {
                    DownLoadState.SUCCESS
                    listener?.invoke(DownLoadState.SUCCESS, uri)
                }
            }
        }
    }

    private val binder by lazy { Stub() }
    private var listener: ((DownLoadState, Uri?) -> Unit)? = null

    inner class Stub : Binder() {
        fun addDownloadStateListener(block: (DownLoadState, Uri?) -> Unit) {
            listener = block
        }

        fun start(downloadUrl: String, filename: String) {
            startDownload(downloadUrl, filename)
        }

        fun cancel() {
            downloadId?.let { downloadManager.remove(it) }
        }
    }


    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        LLog.d(TAG, "onCreate: ")
        ContextCompat.registerReceiver(
            this,
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LLog.d(TAG, "onDestroy: ")
        unregisterReceiver(downloadReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        listener?.invoke(DownLoadState.IDLE, null)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startDownload(url: String, filename: String) {
        require(url.isNotBlank()) { "url must not blank" }
        require(filename.isNotBlank()) { "filename must not blank" }

        LLog.d(TAG, "startDownload: url = $url, filename = $filename")
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        }
        downloadId = downloadManager.enqueue(request)
        listener?.invoke(DownLoadState.DOWNLOADING, null)
    }
}