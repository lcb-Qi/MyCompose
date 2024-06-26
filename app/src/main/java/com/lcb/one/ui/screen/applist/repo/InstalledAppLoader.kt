package com.lcb.one.ui.screen.applist.repo

import android.content.Context
import android.content.pm.ApplicationInfo
import com.lcb.one.ui.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.Locale

suspend fun loadInstalledApps(context: Context = MyApp.getAppContext()) =
    withContext(Dispatchers.IO) {
        val collator = Collator.getInstance(Locale.getDefault()).apply {
            strength = Collator.PRIMARY
            decomposition = Collator.CANONICAL_DECOMPOSITION
        }

        val pm = context.packageManager

        pm.getInstalledPackages(0)
            .asSequence()
            .map {
                val packageName = it.packageName
                val label = it.applicationInfo.loadLabel(pm)
                val icon = it.applicationInfo.loadIcon(pm)
                val isSystemApp = it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0

                AppInfo(packageName, label.toString(), icon, isSystemApp)
            }
            .sortedWith { o1, o2 ->
                collator.compare(o1.label, o2.label)
            }
            .toList()
    }