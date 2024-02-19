package com.lcb.one.ui.page

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.lcb.one.ui.MyApp
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.util.android.SharedPrefUtils
import kotlinx.coroutines.delay

@Composable
@Preview
fun HomePage(
    modifier: Modifier = Modifier,
    onImageClick: (() -> Unit)? = null,
    onTextClick: (() -> Unit)? = null
) {
    var uri by remember { mutableStateOf(SharedPrefUtils.getString("image_url").toUri()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                uri = it
                SharedPrefUtils.putString("image_url", uri.toString())
                MyApp.getAppContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        })

    val startTime = "2020-12-23 00:00:00"
    var durationText by remember {
        mutableStateOf(DateTimeUtils.friendlyDuration(DateTimeUtils.toMillis(startTime)))
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(model = uri, contentDescription = "",
            Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { launcher.launch("image/*") }
        )
        Text(text = durationText)
    }

    LaunchedEffect(key1 = "updater", block = {
        while (true) {
            durationText =
                DateTimeUtils.friendlyDuration(DateTimeUtils.toMillis(startTime))
            delay(1000)
        }
    })
}