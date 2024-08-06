package com.lcb.one.ui.screen.menstruationAssistant

import android.os.Bundle
import androidx.compose.animation.core.animate
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.Screen
import com.lcb.one.ui.widget.appbar.ToolBar
import com.lcb.one.ui.widget.dialog.SimpleMessageDialog
import com.lcb.one.util.android.ToastUtils
import com.lcb.one.ui.screen.menstruationAssistant.repo.MenstruationViewModel
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.averageDurationDay
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.averageIntervalDay
import com.lcb.one.ui.screen.menstruationAssistant.widget.MenstrualCycleHistoryCard
import com.lcb.one.ui.screen.menstruationAssistant.widget.MenstruationMenu
import com.lcb.one.ui.screen.menstruationAssistant.widget.MenstruationMenuAction
import com.lcb.one.ui.screen.menstruationAssistant.widget.PastMcDayPicker
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.util.android.Res
import com.lcb.one.util.android.inputStream
import com.lcb.one.util.android.rememberLauncherForGetContent
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val DRAG_OFFSET_MAX = 400

object MenstruationHistoryScreen : Screen {
    override val route: String
        get() = "MenstruationHistory"

    override val label: String
        get() = Res.string(R.string.menstruation_history)

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
        val mcViewmodel = viewModel<MenstruationViewModel>()
        val allMcDay by mcViewmodel.all.collectAsState(emptyList())
        var showImportButton by remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                ToolBar(
                    title = label,
                    actions = {
                        var showMenu by remember { mutableStateOf(false) }
                        val launcher = rememberLauncherForGetContent {
                            it?.inputStream()?.use { input ->
                                mcViewmodel.import(input)
                            }
                        }

                        AppIconButton(icon = Icons.Rounded.MoreVert, onClick = { showMenu = true })

                        MenstruationMenu(showMenu, { showMenu = false }) {
                            when (it) {
                                MenstruationMenuAction.IMPORT -> launcher.launch("text/plain")
                                MenstruationMenuAction.EXPORT -> mcViewmodel.export()
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showImportButton = true }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                reverseLayout = true
            ) {
                items(items = allMcDay, key = { it.startTime }) { mcDay ->
                    HistoryItem(data = mcDay) { mcViewmodel.deleteMenstruationDay(mcDay) }
                }

                item { SummaryMessage(allMcDay) }
            }
        }

        PastMcDayPicker(showImportButton, onCancel = { showImportButton = false }) { selectRange ->
            val hasIntersect = allMcDay.any {
                !(selectRange.last < it.startTime || selectRange.first > it.endTime)
            }
            if (hasIntersect) {
                ToastUtils.showToast(Res.string(R.string.conflict_tips))
            } else {
                mcViewmodel.addPastMenstruationDay(selectRange.first, selectRange.last)
            }
            showImportButton = false
        }
    }

    @Composable
    fun HistoryItem(data: MenstruationDay, onDelete: (MenstruationDay) -> Unit) {
        var showDeleteDialog by remember { mutableStateOf(false) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        val draggableState = rememberDraggableState {
            offsetX = (offsetX + it).coerceIn(0f, Float.MAX_VALUE)
            if (offsetX > DRAG_OFFSET_MAX) {
                showDeleteDialog = true
            }
        }
        val scope = rememberCoroutineScope()
        val animateOffset: (target: Float) -> Unit = {
            scope.launch {
                animate(initialValue = offsetX, targetValue = it) { value, _ ->
                    offsetX = value
                }
            }
        }
        MenstrualCycleHistoryCard(
            modifier = Modifier
                .offset { IntOffset(-offsetX.roundToInt(), 0) }
                .draggable(
                    enabled = !showDeleteDialog,
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true,
                    onDragStopped = {
                        if (!showDeleteDialog) {
                            animateOffset(0f)
                        }
                    }
                ),
            mcDay = data,
        )

        SimpleMessageDialog(
            show = showDeleteDialog,
            message = stringResource(R.string.confirm_delete),
            onCancel = {
                showDeleteDialog = false
                animateOffset(0f)
            },
            onConfirm = {
                onDelete(data)
                showDeleteDialog = false
            },
            icon = { Icon(Icons.Rounded.WarningAmber, null) }
        )
    }

    @Composable
    fun SummaryMessage(data: List<MenstruationDay>) {
        if (data.isNotEmpty()) {
            val count = data.size
            val averageDuration = data.averageDurationDay()
            val averageInterval = data.averageIntervalDay()

            Column {
                ProvideTextStyle(value = MaterialTheme.typography.bodyLarge) {
                    Text(text = stringResource(R.string.sum_of_records, count))
                    Text(text = stringResource(R.string.average_duration, averageDuration))
                    Text(text = stringResource(R.string.average_interval, averageInterval))
                }
            }
        }
    }
}