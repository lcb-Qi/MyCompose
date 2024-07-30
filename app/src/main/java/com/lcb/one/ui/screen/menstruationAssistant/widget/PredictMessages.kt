package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.lcb.one.R
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
                    text = stringResource(R.string.menstruation_prediction),
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
                        stringResource(
                            R.string.next_menstruation_info,
                            DateTimeUtils.format(predictMcDay.startTime, "yyyy.MM.dd"),
                            days
                        )
                    } else if (predictMcDay != null && selectedDate in predictMcDay.startTime..predictMcDay.endTime) {
                        val days =
                            DateTimeUtils.getDurationDays(
                                predictMcDay.startTime,
                                selectedDate
                            )
                        stringResource(
                            R.string.next_menstruation_days,
                            days + 1
                        )
                    } else {
                        stringResource(R.string.no_more_info)
                    }
                Text(text = message, style = MaterialTheme.typography.labelLarge)
            }
        )
    }
}