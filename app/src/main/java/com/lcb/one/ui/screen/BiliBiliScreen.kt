package com.lcb.one.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.lcb.one.ui.widget.AppBar
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.viewmodel.BiliViewModel
import kotlinx.coroutines.launch

@Composable
fun BiliBiliScreen() {
    Scaffold(topBar = { AppBar(title = stringResource(R.string.bibibili)) }) { paddingValues ->

        var textInput by remember { mutableStateOf("") }
        val biliViewModel = viewModel<BiliViewModel>()
        val coverUrl by biliViewModel.coverUrl.observeAsState()
        val isLoading by biliViewModel.isLoading.collectAsState()
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
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
                Button(
                    onClick = { biliViewModel.getUrlByVideoId(if (BuildConfig.DEBUG) "BV117411r7R1" else textInput) }
                ) {
                    Text(text = stringResource(R.string.obtain), maxLines = 1)
                }
            }

            Box(
                Modifier.height(150.dp),
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

            Button(
                enabled = !coverUrl.isNullOrBlank(),
                onClick = {
                    coroutineScope.launch {
                        DownLoadUtil.saveImageFromUrl(coverUrl!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.save), maxLines = 1)
            }
        }
    }
}