package com.lcb.one.ui.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.PendingIntentCompat
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.activity.MainActivity
import com.lcb.one.ui.screen.main.repo.model.PoemInfo
import com.lcb.one.util.android.LLog
import com.lcb.one.util.android.PACKAGE_ME
import com.lcb.one.util.android.UserPrefManager
import com.lcb.one.util.common.JsonUtils

class PoemAppWidgetProvider : AppWidgetProvider() {
    companion object {
        private const val TAG = "PoemAppWidget"
        private const val ACTION_DATA_CHANGED = "com.lcb.one.appwidget.poem.ACTION_DATA_CHANGED"

        fun tryUpdate(context: Context = MyApp.getAppContext()) {
            val intent = Intent().apply {
                setPackage(PACKAGE_ME)
                setAction(ACTION_DATA_CHANGED)
            }
            context.sendBroadcast(intent)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        LLog.d(TAG, "onReceive: action = $action")

        when (action) {
            ACTION_DATA_CHANGED -> refreshUi(context)
            else -> {}
        }
        super.onReceive(context, intent)
    }

    private fun refreshUi(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        val cn = ComponentName(context, PoemAppWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(cn)

        appWidgetManager.updateAppWidget(ids, createRemoteViews(context))
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        LLog.d(TAG, "onUpdate: ids = ${appWidgetIds.contentToString()}")

        appWidgetManager.updateAppWidget(appWidgetIds, createRemoteViews(context))
    }

    private fun createRemoteViews(context: Context): RemoteViews {
        val remoteViews =
            RemoteViews(context.packageName, R.layout.layout_poem_appwidget)

        remoteViews.setOnClickPendingIntent(R.id.container, createPendingIntent(context))

        val lastPoem = UserPrefManager.getString(UserPrefManager.Key.POEM_LAST)
        val poemInfo = JsonUtils.fromJson<PoemInfo>(lastPoem)

        poemInfo?.let {
            remoteViews.setTextViewText(R.id.tv_content, it.recommend)
            remoteViews.setTextViewText(
                R.id.tv_author,
                "—— ${it.origin.author}《${it.origin.title}》"
            )
        }

        return remoteViews
    }

    private fun createPendingIntent(context: Context): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntentCompat.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT,
            false
        )
        return pendingIntent
    }
}