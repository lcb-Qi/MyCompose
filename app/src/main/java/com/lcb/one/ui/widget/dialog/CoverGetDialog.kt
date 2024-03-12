package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.lcb.one.R
import com.lcb.one.ui.theme.titleMedium
import com.lcb.one.ui.widget.ToolButton
import com.lcb.one.viewmodel.BiliViewModel

@Composable
fun CoverGetDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var textInput by remember { mutableStateOf("BV117411r7R1") }
    val biliViewModel = viewModel<BiliViewModel>()
    val coverUrl by biliViewModel.coverUrl.collectAsState()
    val isLoading by biliViewModel.isLoading.collectAsState()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "输入完整的BV或av号", style = titleMedium()) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text(text = "BV或av号") },
                        modifier = Modifier.weight(1f)
                    )
                    ToolButton(text = stringResource(R.string.obtain)) {
                        biliViewModel.getCoverUrl(textInput)
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
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.back))
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(coverUrl) }) {
                Text(text = stringResource(R.string.save))
            }
        }
    )
}