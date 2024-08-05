package com.lcb.one.ui.screen.main.home

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lcb.one.R
import com.lcb.one.ui.widget.common.noRippleClickable
import com.lcb.one.ui.widget.settings.storage.disk.rememberStringPrefState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.util.android.PhoneUtil
import com.lcb.one.util.android.UserPref
import com.lcb.one.util.android.rememberLauncherForGetContent

private val horizontalPadding = 16.dp

@Composable
private fun calculateImageSize() = with(LocalDensity.current) {
    val width = PhoneUtil.getScreenWidth().toDp() - horizontalPadding * 2
    val height = width / 16 * 9

    DpSize(width, height)
}

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            var uri: String by rememberStringPrefState(UserPref.Key.HOME_HEADER_BG, "")

            val launcher = rememberLauncherForGetContent {
                it?.run {
                    context.contentResolver.takePersistableUriPermission(
                        this,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    uri = toString()
                }
            }

            val maxSize = calculateImageSize()
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .build(),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .requiredSizeIn(
                        minWidth = 48.dp, minHeight = 48.dp,
                        maxWidth = maxSize.width, maxHeight = maxSize.height
                    )
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.small)
                    .noRippleClickable { launcher.launch("image/*") }
            )
        }

        item { LoveCard() }
        item { ShortcutsCard(navController) }
    }
}