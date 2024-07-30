package com.lcb.one.ui.widget.dialog

import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lcb.one.R
import com.lcb.one.ui.AppGlobalConfigs
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun AssertInternetDialog(show: Boolean) {
    if (!show) return

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            AppTextButton(
                text = stringResource(R.string.go_settings),
                onClick = {
                    val intent =
                        Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    MyApp.get().startActivity(intent)
                    AppGlobalConfigs.assertNetwork = false
                }
            )
        },
        dismissButton = {
            AppTextButton(
                text = stringResource(R.string.cancel),
                onClick = { AppGlobalConfigs.assertNetwork = false }
            )
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
                text = stringResource(R.string.msg_no_internet),
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}