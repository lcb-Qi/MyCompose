package com.lcb.one.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.lcb.one.viewmodel.BiliViewModel
import com.lcb.one.ui.widget.ToolButton
import com.lcb.one.util.android.DownLoadUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BiliPage() {
    var textInput by remember { mutableStateOf("BV117411r7R1") }
    val biliViewModel = viewModel<BiliViewModel>()
    val coverUrl by biliViewModel.coverUrl.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            placeholder = { Text(text = "请输入bv(av)号") },
            modifier = Modifier.fillMaxWidth()
        )

        ToolButton(title = "获取封面") { biliViewModel.getVideoInfo(textInput) }

        AsyncImage(model = coverUrl, contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .combinedClickable(onLongClick = { saveCover(coverUrl) }) {}
        )
    }
}

private fun saveCover(url: String) {
    CoroutineScope(Dispatchers.IO).launch {
        DownLoadUtil.saveImageFromUrl(url)
    }
}


