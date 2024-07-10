package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.lcb.one.localization.Localization
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.util.common.DateTimeUtils

@Composable
fun PredictMessages(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    predictMcDay: MenstruationDay?
) {
    Card(modifier = modifier.fillMaxWidth()) {
        ListItem(
            colors = SettingsDefaults.colorOnCard(),
            headlineContent = {
                Text(
                    text = Localization.menstruationPrediction,
                    fontWeight = FontWeight.Medium
                )
            },
            supportingContent = {
                val message =
                    if (predictMcDay != null && selectedDate < predictMcDay.startTime) {
                        val days =
                            DateTimeUtils.getDurationDays(
                                DateTimeUtils.nowMillis(),
                                predictMcDay.startTime
                            )
                        String.format(
                            Localization.nextMenstruationInfo,
                            DateTimeUtils.format(predictMcDay.startTime, "yyyy.MM.dd"),
                            days
                        )
                    } else if (predictMcDay != null && selectedDate in predictMcDay.startTime..predictMcDay.endTime) {
                        val days =
                            DateTimeUtils.getDurationDays(
                                predictMcDay.startTime,
                                selectedDate
                            )
                        String.format(Localization.nextMenstruationDays, days + 1)
                    } else {
                        "暂无更多信息"
                    }
                Text(text = message, style = MaterialTheme.typography.labelLarge)
            }
        )
    }
}