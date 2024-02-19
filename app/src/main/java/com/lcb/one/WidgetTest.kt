package com.lcb.one

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcb.one.ui.theme.bodyMedium
import com.lcb.one.ui.theme.bodySmall
import com.lcb.one.ui.theme.titleMedium

@Preview
@Composable
fun AlertDialogTest() {
    val title = "过华清宫绝句三首"
    val author = "杜牧"
    val dynasty = "唐代"
    val content = listOf(
        "新丰绿树起黄埃，数骑渔阳探使回。",
        "霓裳一曲千峰上，舞破中原始下来。",
        "万国笙歌醉太平，倚天楼殿月分明。",
        "云中乱拍禄山舞，风过重峦下笑声。",
        "春归何处。寂寞无行路。若有人知春去处。唤取归来同住。",
        "春无踪迹谁知。除非问取黄鹂。百啭无人能解，因风飞过蔷薇。"
    )
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            ClickableText(text = AnnotatedString(stringResource(R.string.ok)), onClick = { })
        },
        title = { Text(text = "详情") },
        text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = title, style = titleMedium())
                Text(text = "$dynasty $author", style = bodySmall())
                for (sentence in content) {
                    Text(
                        onTextLayout = {
                            if (it.lineCount > 1) {
                                it.layoutInput.style.copy(textIndent = TextIndent(firstLine = 24.sp))
                            }
                        },
                        text = sentence,
                        style = bodyMedium().copy(/* textIndent = TextIndent(firstLine = 24.sp) */)
                    )
                }

            }
        })
}
