package com.lcb.one.ui.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.lcb.one.ui.activity.MainActivity
import com.lcb.one.util.android.SharedPrefUtils
import com.lcb.one.util.common.JsonUtils
import com.lcb.one.viewmodel.PoemViewModel
import kotlinx.coroutines.delay

class PoemAppWidget : GlanceAppWidget() {
    companion object {
        private const val TAG = "PoemAppWidget"
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        delay(1000)
        provideContent {
            PoemAppWidgetContent()
        }
    }

    @Composable
    private fun PoemAppWidgetContent() {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))
                    .padding(16.dp)
                    .clickable(actionStartActivity<MainActivity>()),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                val lastPoem = SharedPrefUtils.getString(PoemViewModel.KEY_LAST_POEM)
                val poemInfo = JsonUtils.fromJson<PoemViewModel.PoemInfo>(lastPoem)
                val recommend = poemInfo?.recommend ?: ""
                val author = "—— ${poemInfo?.origin?.dynasty} ${poemInfo?.origin?.author}"
                Text(
                    text = recommend,
                    modifier = GlanceModifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                    )
                )

                Text(
                    text = author,
                    modifier = GlanceModifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                    )
                )
            }
        }
    }
}

class GlanceReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = PoemAppWidget()
}