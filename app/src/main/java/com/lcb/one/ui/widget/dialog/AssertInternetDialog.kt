package com.lcb.one.ui.widget.dialog

import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.MyApp

@Composable
fun AssertInternetDialog(show: Boolean) {
    if (!show) return

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                val intent =
                    Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                MyApp.getAppContext().startActivity(intent)
                AppGlobalConfigs.assertNetwork = false
            }) {
                Text(text = "前往设置")
            }
        },
        dismissButton = {
            TextButton(onClick = { AppGlobalConfigs.assertNetwork = false }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                text = "无法连接网络，请检查网络后重试",
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}