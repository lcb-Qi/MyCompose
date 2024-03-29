package com.lcb.one.ui.screen.bilibili

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.lcb.one.BuildConfig
import com.lcb.one.R
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.util.android.DownLoadUtil
import com.lcb.one.viewmodel.BiliViewModel
import kotlinx.coroutines.launch

@Composable
fun BiliBiliScreen() {
    Scaffold(topBar = { ToolBar(title = stringResource(R.string.bibibili)) }) { paddingValues ->

        var textInput by remember { mutableStateOf("") }
        val biliViewModel = viewModel<BiliViewModel>()
        val coverUrl by biliViewModel.coverUrl.observeAsState()
        val coroutineScope = rememberCoroutineScope()

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
        ) {
            val guide = createRef()
            Column(modifier = Modifier
                .constrainAs(guide) {}
                .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "请输入av或BV号以获取封面",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    singleLine = true,
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text(text = "如BV117411r7R1") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { biliViewModel.getUrlByVideoId(textInput) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.obtain))
                }

                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(coverUrl)
                        .placeholder(R.drawable.icon_bilibili)
                        .error(R.drawable.icon_bilibili)
                        .build(),
                    contentDescription = "",
                    loading = { CircularProgressIndicator() }
                )
            }

            val save = createRef()
            if (!coverUrl.isNullOrBlank()) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            DownLoadUtil.saveImageFromUrl(coverUrl!!)
                        }
                    },
                    modifier = Modifier
                        .constrainAs(save) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(end = 16.dp, bottom = 16.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Save, contentDescription = "")
                }
            }
        }
    }
}