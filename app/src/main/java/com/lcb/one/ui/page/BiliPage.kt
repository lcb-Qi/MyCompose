package com.lcb.one.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.lcb.one.viewmodel.BiliViewModel
import com.lcb.one.ui.widget.ToolButton
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.util.android.LLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BiliPage() {
    var show by remember { mutableStateOf(false) }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ToolButton(text = "获取封面") { show = true }
    }

    if (show) {
        CoverDialog(onDismiss = { show = false }, onSave = { saveCover(it) })
    }
}

private fun saveCover(url: String) {
    CoroutineScope(Dispatchers.IO).launch {
        DownLoadUtil.saveImageFromUrl(url)
    }
}

@Composable
fun CoverDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var textInput by remember { mutableStateOf("BV117411r7R1") }
    val biliViewModel = viewModel<BiliViewModel>()
    val coverUrl by biliViewModel.coverUrl.collectAsState()

    val isLoading by biliViewModel.isLoading.collectAsState()
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text(text = "BV或av号") },
                        modifier = Modifier.weight(1f)
                    )
                    ToolButton(text = "获取") {
                        // isLoading = true
                        biliViewModel.getCoverUrl(textInput)
                    }
                }


                Box(
                    Modifier.height(150.dp),
                    Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AsyncImage(model = coverUrl, contentDescription = null)
                    }
                }

            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(coverUrl) }) {
                Text(text = "save")
            }
        }
    )
}


