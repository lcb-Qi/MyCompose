package com.lcb.one.ui.screen.main.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.screen.main.repo.model.PoemResponse
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun PoemInfoDialog(
    show: Boolean,
    origin: () -> PoemResponse.Data.Origin,
    onDismiss: () -> Unit,
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            AppTextButton(text = Localization.ok, onClick = onDismiss)
        },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = origin().title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${origin().dynasty} ${origin().author}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            // TODO: 2024/3/4 添加滚动条
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(origin().content) {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}