package com.lcb.one.ui.screen.about.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun AboutSaltFishDialog(show: Boolean, onDismiss: () -> Unit) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            AppTextButton(text = stringResource(R.string.ok), onClick = onDismiss)
        },
        title = { AppLabel() },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                KeyValueText("application id", BuildConfig.APPLICATION_ID)
                KeyValueText("version", "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})")
                KeyValueText("build type", BuildConfig.BUILD_TYPE)
                KeyValueText("build time", stringResource(R.string.BUILD_TIME))
                KeyValueText("kotlin version", KotlinVersion.CURRENT.toString())
            }
        }
    )
}

@Composable
private fun AppLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            Image(
                painter = painterResource(R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Text(text = stringResource(R.string.app_name))
        }
    )
}

@Composable
fun KeyValueText(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = key, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
        Text(text = value, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}