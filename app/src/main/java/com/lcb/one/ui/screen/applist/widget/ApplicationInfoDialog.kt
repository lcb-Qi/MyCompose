package com.lcb.one.ui.screen.applist.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lcb.one.localization.Localization
import com.lcb.one.util.android.AppUtils
import com.lcb.one.util.android.StorageUtils
import com.lcb.one.util.android.ToastUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationInfoDialog(show: Boolean, packageName: String, onDisMiss: () -> Unit) {
    if (!show || packageName.isBlank()) return

    val pm = LocalContext.current.packageManager
    val packageInfo = pm.getPackageInfo(packageName, 0)
    val label = packageInfo.applicationInfo.loadLabel(pm).toString()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(onDismissRequest = onDisMiss) {
        Column(
            Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.size(50.dp),
                    model = packageInfo.applicationInfo.loadIcon(pm),
                    contentDescription = ""
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            ToolButton(
                text = Localization.extractIcon,
                leadingIcon = Icons.Rounded.Image,
                onclick = {
                    scope.launch {
                        val icon = AppUtils.getAppIcon(packageName = packageName)
                        if (icon != null) {
                            StorageUtils.createImageFile(icon, "${label}_logo")
                                .onSuccess { ToastUtils.showToast("${Localization.saveSuccess} $it") }
                                .onFailure { ToastUtils.showToast(Localization.saveFailed) }
                        } else {
                            ToastUtils.showToast(Localization.saveFailed)
                        }
                    }
                }
            )

            ToolButton(
                text = Localization.extractPackage,
                leadingIcon = Icons.Rounded.Android,
                onclick = {
                    scope.launch {
                        StorageUtils.saveApk(AppUtils.getApkPath(packageName), label)
                            .onSuccess { ToastUtils.showToast("${Localization.saveSuccess} $it") }
                            .onFailure { ToastUtils.showToast(Localization.saveFailed) }
                    }
                }
            )
        }
    }
}