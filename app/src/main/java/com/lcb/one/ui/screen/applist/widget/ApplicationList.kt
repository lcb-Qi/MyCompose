package com.lcb.one.ui.screen.applist.widget

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.dialog.LoadingDialog
import com.lcb.one.util.common.ThreadUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.Locale

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val isSystemApp: Boolean
)

enum class AppType(val label: String) {
    USER("用户应用"), SYSTEM("系统应用"), ALL("所有应用");

    companion object {
        fun from(id: Int) = AppType.entries.first { it.ordinal == id }
    }
}

// TODO: 性能优化、应用信息、提取图标/apk
@Composable
fun ApplicationList(
    modifier: Modifier = Modifier,
    displayType: AppType,
    filterText: String = "",
    onItemClick: (String) -> Unit = {}
) {
    var init by remember { mutableStateOf(false) }
    var all by remember {
        mutableStateOf(emptyList<AppInfo>())
    }

    LaunchedEffect(Unit) {
        if (init) return@LaunchedEffect
        withContext(Dispatchers.IO) {
            all = init()
            init = true
        }
    }

    val filterAppType: (AppInfo) -> Boolean = {
        (it.isSystemApp && displayType != AppType.USER) || (!it.isSystemApp && displayType != AppType.SYSTEM)
    }

    val filterKeyWord: (AppInfo) -> Boolean = {
        filterText.isBlank()
                || it.packageName.contains(filterText, true)
                || it.label.contains(filterText, true)
    }

    LazyColumn(modifier = modifier) {
        for (appInfo in all) {
            if (!filterAppType(appInfo))
                continue
            if (!filterKeyWord(appInfo))
                continue

            item(appInfo.packageName) {
                ListItem(
                    modifier = Modifier.clickable { onItemClick(appInfo.packageName) },
                    leadingContent = {
                        AsyncImage(
                            modifier = Modifier.size(24.dp),
                            model = appInfo.icon,
                            contentDescription = ""
                        )
                    },
                    headlineContent = {
                        Text(
                            text = appInfo.label,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    supportingContent = {
                        Text(
                            text = appInfo.packageName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }

    LoadingDialog(show = !init)
}

private fun init(): List<AppInfo> {
    ThreadUtils.assertOnBackgroundThread()

    val collator = Collator.getInstance(Locale.getDefault()).apply {
        strength = Collator.PRIMARY
        decomposition = Collator.CANONICAL_DECOMPOSITION
    }

    val pm = MyApp.getAppContext().packageManager

    return pm.getInstalledPackages(0).asSequence()
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