package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.lcb.one.R
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.getMcDay
import com.lcb.weight.settings.SettingsDefaults
import com.lcb.one.util.common.DateTimeUtils

@Composable
fun MenstruationInfoCard(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    mcDays: List<MenstruationDay>
) {
    Card(modifier = modifier.fillMaxWidth()) {
        ListItem(
            colors = SettingsDefaults.colorsOnCard(),
            headlineContent = {
                val date = DateTimeUtils.format(selectedDate, DateTimeUtils.FORMAT_ONLY_DATE)
                Text(text = date, fontWeight = FontWeight.Medium)
            },
            supportingContent = {
                val mcDay = mcDays.getMcDay(selectedDate)
                val message = if (mcDay == null) {// 1.所选日期不存在月经记录
                    stringResource(R.string.no_menstruation)
                } else {// 2.所选日期存在月经记录
                    // 展示第几天
                    val duration = DateTimeUtils.getDurationDays(mcDay.startTime, selectedDate)
                    stringResource(R.string.menstruation_days, duration + 1)
                }
                Text(text = message)
            }
        )
    }
}