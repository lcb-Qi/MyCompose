package com.lcb.one.ui.screen.qmc

import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.AppButton
import com.lcb.one.ui.widget.dialog.LoadingDialog
import com.lcb.one.util.android.Res
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.android.getRelativePath
import com.lcb.one.util.android.rememberLauncherForGetContents
import kotlinx.coroutines.launch

object QmcConverterScreen : Screen {
    override val route: String
        get() = "QmcConverter"

    override val label: String
        get() = Res.string(R.string.qmc_converter)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        Scaffold(topBar = { ToolBar(title = label) }) { innerPadding ->

            var loading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            val selectedFile = remember { mutableStateListOf<Uri>() }
            val launcher = rememberLauncherForGetContents {
                selectedFile.clear()
                selectedFile.addAll(it)
            }
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SelectedFiles(selectedFile)
                SupportFormatTips()
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(
                        text = stringResource(R.string.select_file),
                        onClick = { launcher.launch("*/*") },
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        modifier = Modifier.weight(1f),
                        enabled = selectedFile.isNotEmpty(),
                        text = stringResource(R.string.click_to_convert),
                        onClick = {
                            scope.launch {
                                loading = true

                                val total = selectedFile.size
                                var failedCount = 0
                                selectedFile.forEach { uri ->
                                    val result = QmcConverter.convert(uri)
                                    result.onFailure { failedCount++ }
                                }

                                loading = false
                                val msg = Res.string(R.string.qmc_convertor_msg, total, failedCount)
                                ToastUtils.showToast(msg)
                            }
                        }
                    )
                }
            }

            LoadingDialog(loading)
        }
    }

    @Composable
    private fun SelectedFiles(selectedFile: SnapshotStateList<Uri>) {
        OutlinedCard {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.selected_files),
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    modifier = Modifier.height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(items = selectedFile, key = { it }) { fileUri ->
                        Text(
                            text = fileUri.getRelativePath(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SupportFormatTips() {
        Card {
            Box(modifier = Modifier.padding(16.dp)) {
                val guideMessage = remember {
                    buildString {
                        appendLine(Res.string(R.string.supported_formats))
                        val supportFormat = QmcConverter.qmcFormatMap.keys.joinToString()
                        append(supportFormat)
                    }
                }
                Text(text = guideMessage)
            }
        }
    }
}