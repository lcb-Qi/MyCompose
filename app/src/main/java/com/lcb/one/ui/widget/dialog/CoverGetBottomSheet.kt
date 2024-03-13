package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.widget.ToolButton
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.viewmodel.BiliViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoverGetBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (!show) return

    var textInput by remember { mutableStateOf("") }
    val biliViewModel = viewModel<BiliViewModel>()
    val coverUrl by biliViewModel.coverUrl.observeAsState()
    val isLoading by biliViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    biliViewModel.getUrlByVideoId(textInput)
    ModalBottomSheet(sheetState = sheetState, onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "请输入av或BV号以获取封面", style = MaterialTheme.typography.titleMedium)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    singleLine = true,
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text(text = "如BV117411r7R1") },
                    modifier = Modifier.weight(1f)
                )
                ToolButton(text = stringResource(R.string.obtain)) {
                    biliViewModel.getUrlByVideoId(if (BuildConfig.DEBUG) "BV117411r7R1" else textInput)
                }
            }

            Box(
                Modifier.height(200.dp),
                Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImage(
                        model = coverUrl,
                        contentDescription = "",
                        loading = { CircularProgressIndicator() }
                    )
                }
            }

            val enable = !coverUrl.isNullOrBlank()
            ToolButton(
                enabled = enable,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.save)
            ) {
                coroutineScope.launch {
                    DownLoadUtil.saveImageFromUrl(coverUrl!!)
                }
            }
        }
    }
}