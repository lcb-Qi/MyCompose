package com.lcb.one.ui.screen.menstruationAssistant

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
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.MainActivity
import com.lcb.one.util.android.LLogger
import java.util.concurrent.Executors

class MenstruationAssistantQsService : TileService() {
    companion object {
        private const val TAG = "MenstruationAssistantQsService"
        fun tryAddQs(context: Context = MyApp.get()) {
            LLogger.debug(TAG) { "tryAddQs: " }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                return
            val statusBarManager = context.getSystemService(StatusBarManager::class.java)
            statusBarManager.requestAddTileService(
                ComponentName(context, MenstruationAssistantQsService::class.java),
                context.getString(R.string.menstruation_assistant),
                Icon.createWithResource(context.packageName, R.drawable.ic_ice_cream),
                Executors.newSingleThreadExecutor()
            ) {
                LLogger.debug(TAG) { "tryAddQs: result = $it" }
            }
        }

    }

    override fun onTileAdded() {
        super.onTileAdded()
        LLogger.debug(TAG) { "onTileAdded: " }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        LLogger.debug(TAG) { "onTileRemoved: " }
    }

    override fun onClick() {
        LLogger.debug(TAG) { "onClick: " }
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra(MainActivity.EXT_START_ROUTE, MenstruationAssistantScreen.route)

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