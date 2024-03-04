package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.bean.PoemResponse
import com.lcb.one.ui.theme.bodyMedium
import com.lcb.one.ui.theme.bodySmall
import com.lcb.one.ui.theme.titleLarge

@Composable
fun PoemInfoDialog(
    poemDetail: PoemResponse.Data.Origin,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(text = stringResource(R.string.ok)) }
        },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = poemDetail.title, style = titleLarge())
                Text(text = "${poemDetail.dynasty} ${poemDetail.author}", style = bodySmall())
            }
        },
        text = {
            // TODO: 2024/3/4 添加滚动条
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(poemDetail.content) {
                    Text(text = it, style = bodyMedium())
                }
            }
        }
    )
}

@Composable
fun LoadingDialog(content: @Composable () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator()
                content()
            }

        }
    )
}