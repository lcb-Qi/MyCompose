package com.lcb.one.ui.screen.mcAssistant

import androidx.compose.animation.core.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcb.one.bean.McDay
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.common.Point
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.util.common.DateTimeUtils
import com.lcb.one.viewmodel.MenstrualCycleViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import kotlin.math.roundToInt

private const val DRAG_OFFSET_MAX = 400

@Composable
fun MenstrualCycleHistoryScreen() {
    val mcViewmodel = viewModel<MenstrualCycleViewModel>()
    val mcDays by mcViewmodel.getAll().collectAsState(emptyList())
    var showAdd by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { ToolBar(title = "全部经期") },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = true
        ) {
            items(count = mcDays.size, key = { mcDays[it].startTime }) { index ->
                var showDelete by remember { mutableStateOf(false) }
                var offsetX by remember { mutableFloatStateOf(0f) }
                val draggableState = rememberDraggableState {
                    offsetX = (offsetX + it).coerceIn(0f, Float.MAX_VALUE)
                    if (offsetX > DRAG_OFFSET_MAX) {
                        showDelete = true
                    }
                }
                MenstrualCycleHistoryCard(
                    modifier = Modifier
                        .draggable(
                            enabled = !showDelete,
                            state = draggableState,
                            orientation = Orientation.Horizontal,
                            reverseDirection = true,
                            onDragStopped = {
                                if (!showDelete) {
                                    animate(offsetX, 0f) { value, velocity ->
                                        offsetX = value
                                    }
                                }
                            }
                        )
                        .offset {
                            IntOffset(-offsetX.roundToInt(), 0)
                        },
                    mcDay = mcDays[index]
                )

                SimpleMessageDialog(
                    show = showDelete,
                    message = "确认删除这条记录？",
                    onCancel = {
                        showDelete = false
                        scope.launch {
                            animate(offsetX, 0f) { value, velocity ->
                                offsetX = value
                            }
                        }
                    },
                    onConfirm = {
                        mcViewmodel.deleteMenstrualCycle(mcDays[index])
                        showDelete = false
                    },
                    icon = {
                        Icon(imageVector = Icons.Rounded.WarningAmber, contentDescription = "")
                    }
                )
            }
        }
    }

    PastMcDayPicker(showAdd, onCancel = { showAdd = false }) { selectRange ->
        val hasIntersect = mcDays.any {
            !(selectRange.last < it.startTime || selectRange.first > it.endTime)
        }
        if (hasIntersect) {
            ToastUtils.showToast("和已有记录冲突")
        } else {
            mcViewmodel.addPastMenstrualCycle(selectRange.first, selectRange.last)
        }
        showAdd = false
    }
}

@Composable
fun MenstrualCycleHistoryCard(modifier: Modifier = Modifier, mcDay: McDay) {
    val startTime = DateTimeUtils.toLocalDate(mcDay.startTime)
    val endTime = if (mcDay.finish) {
        DateTimeUtils.toLocalDate(mcDay.endTime)
    } else {
        null
    }
    val duration =
        Duration.between(
            startTime.atStartOfDay(),
            endTime?.atStartOfDay() ?: LocalDate.now().atStartOfDay()
        )
    ListItem(
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape),
        leadingContent = if (!mcDay.finish) {
            { Point() }
        } else {
            null
        },
        headlineContent = { Text(text = "经期", style = MaterialTheme.typography.titleMedium) },
        supportingContent = {
            Text(
                text = "$startTime 至 ${endTime ?: "未结束"}",
                style = MaterialTheme.typography.titleSmall
            )
        },
        trailingContent = {
            Text(
                text = "共${duration.toDays() + 1}天",
                style = MaterialTheme.typography.titleSmall
            )
        }
    )
}