package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lcb.one.bean.PoemResponse
import com.lcb.one.ui.theme.bodyMedium
import com.lcb.one.ui.theme.bodySmall
import com.lcb.one.ui.theme.titleLarge

@Composable
fun PoemInfoDialog(
    poemDetail: PoemResponse.Data.Origin,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = {
            val annotatedTitle = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = titleLarge().fontSize)) {
                    append(poemDetail.title)
                }
                appendLine()
                withStyle(SpanStyle(fontSize = bodySmall().fontSize)) {
                    append("${poemDetail.dynasty} ${poemDetail.author}")
                }
            }
            Text(text = annotatedTitle)
        },
        text = {
            LazyColumn(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(poemDetail.content) { sentence ->
                    Text(text = sentence, style = bodyMedium())
                }
            }
        }
    )
}