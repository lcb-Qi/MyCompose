package com.lcb.one.ui.screen.main.home

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lcb.one.R
import com.lcb.one.ui.widget.common.noRippleClickable
import com.lcb.one.ui.widget.settings.storage.disk.rememberStringPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.UserPref
import com.lcb.one.util.android.rememberLauncherForGetContent

@Composable
fun HeaderImage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var uri: String by rememberStringPrefState(UserPref.Key.HOME_HEADER_BG, "")
    val launcher = rememberLauncherForGetContent {
        it?.run {
            context.contentResolver
                .takePersistableUriPermission(this, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            uri = toString()
        }
    }

    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.small)
            .noRippleClickable(onLongClick = { launcher.launch("image/*") }),
        model = ImageRequest.Builder(context)
            .data(uri)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        contentDescription = null
    )
}