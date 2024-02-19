package com.lcb.one.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.R
import com.lcb.one.bean.PoemResponse
import com.lcb.one.ui.page.NoRippleInteractionSource
import com.lcb.one.ui.theme.bodyMedium
import com.lcb.one.ui.theme.bodySmall
import com.lcb.one.ui.theme.labelLarge
import com.lcb.one.ui.theme.primaryColor
import com.lcb.one.ui.theme.titleLarge
import com.lcb.one.ui.theme.titleMedium
import com.lcb.one.viewmodel.PoemViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun AppActionBar() {
    val poemViewModel = viewModel<PoemViewModel>()
    val poemInfo by poemViewModel.poemFlow.collectAsState()
    var showDetail by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = poemInfo.recommend,
                style = titleMedium(),
                modifier = Modifier
                    .wrapContentSize()
                    .combinedClickable(
                        onLongClick = { showDetail = true },
                        indication = LocalIndication.current,
                        interactionSource = NoRippleInteractionSource(),
                    ) {
                        poemViewModel.getPoem(true)
                    }
            )
        }
    )

    if (showDetail) {
        PoemInfoDialog(
            poemInfo.origin,
            onDismissRequest = { showDetail = false },
            onConfirmClick = { showDetail = false }
        )
    }
}

@Composable
fun PoemInfoDialog(
    poemDetail: PoemResponse.Data.Origin,
    onConfirmClick: () -> Unit,
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
        })
}