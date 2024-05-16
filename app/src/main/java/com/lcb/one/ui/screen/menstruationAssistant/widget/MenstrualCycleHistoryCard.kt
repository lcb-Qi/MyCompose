package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.localization.Localization
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.util.common.DateTimeUtils

@Composable
fun MenstrualCycleHistoryCard(
    modifier: Modifier = Modifier,
    mcDay: MenstruationDay,
    onClick: () -> Unit = {}
) {
    val startTime = DateTimeUtils.toLocalDate(mcDay.startTime)
    val endTime = if (mcDay.finish) {
        DateTimeUtils.toLocalDate(mcDay.endTime)
    } else {
        null
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = CardDefaults.shape,
        border = if (mcDay.finish) {
            null
        } else {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        }
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            headlineContent = { Text(text = Localization.menstruation, style = MaterialTheme.typography.titleMedium) },
            supportingContent = {
                Text(
                    text = "$startTime ${Localization.to} ${endTime ?: Localization.going}",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            trailingContent = {
                Text(
                    text = "${mcDay.getDurationDay()} ${Localization.days}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        )
    }
}