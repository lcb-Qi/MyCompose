package com.lcb.one.ui.screen.applist.repo

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable
import com.lcb.one.ui.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.Locale

@Stable
data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val isSystemApp: Boolean
)

suspend fun loadInstalledApps(context: Context = MyApp.get()) = withContext(Dispatchers.IO) {
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