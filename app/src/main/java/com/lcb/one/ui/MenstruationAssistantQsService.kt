package com.lcb.one.ui

import android.app.PendingIntent
import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.TileService
import androidx.core.app.PendingIntentCompat
import com.lcb.one.R
import com.lcb.one.route.destinations.MenstruationAssistantScreenDestination
import com.lcb.one.ui.activity.MainActivity
import com.lcb.one.util.android.LLog
import java.util.concurrent.Executors

class MenstruationAssistantQsService : TileService() {
    companion object {
        fun tryAddQs(context: Context = MyApp.get()) {
            LLog.d(TAG, "tryAddQs: ")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                return
            val statusBarManager = context.getSystemService(StatusBarManager::class.java)
            statusBarManager.requestAddTileService(
                ComponentName(context, MenstruationAssistantQsService::class.java),
                context.getString(R.string.menstruation_assistant),
                Icon.createWithResource(context.packageName, R.drawable.ic_ice_cream),
                Executors.newSingleThreadExecutor()
            ) {
                LLog.d(TAG, "tryAddQs: result = $it")
            }
        }

        private const val TAG = "MenstruationAssistantQsService"
    }

    override fun onTileAdded() {
        super.onTileAdded()
        LLog.d(TAG, "onTileAdded: ")
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        LLog.d(TAG, "onTileRemoved: ")
    }

    override fun onClick() {
        LLog.d(TAG, "onClick: ")
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("route", MenstruationAssistantScreenDestination.route)

        val pendingIntent = PendingIntentCompat.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT,
            false
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (pendingIntent != null) {
                startActivityAndCollapse(pendingIntent)
            }
        } else {
            startActivityAndCollapse(intent)
        }
    }
}