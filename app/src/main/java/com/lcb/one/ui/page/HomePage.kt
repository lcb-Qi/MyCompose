package com.lcb.one.ui.page

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.lcb.one.util.LLog
import com.lcb.one.util.SharedPrefUtils

@Composable
@Preview
fun HomePage(
    modifier: Modifier = Modifier,
    onImageClick: (() -> Unit)? = null,
    onTextClick: (() -> Unit)? = null
) {
    var url by remember { mutableStateOf(SharedPrefUtils.getString("image_url").toUri()) }
    LLog.d("TAG", "HomePage: url = $url")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                LLog.d("TAG", "HomePage: onResult: $it")
                url = it
                SharedPrefUtils.putString("image_url", url.toString())
            }
        })

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(model = url, contentDescription = "",
            Modifier.clickable {
                launcher.launch("image/*")
            },
            onState = {
                LLog.d("TAG", "HomePage: state = $it")
                if (it is AsyncImagePainter.State.Error) {
                    LLog.d("TAG", "HomePage: state Error = $it.")
                }
            }
        )
        Text(text = "xx年xx天yy时xx分xx秒")
    }
}