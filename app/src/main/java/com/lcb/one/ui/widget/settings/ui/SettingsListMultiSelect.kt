package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.common.AppTextButton
import com.lcb.one.ui.widget.settings.storage.SettingValueState

@Composable
fun SettingsListMultiSelect(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<Set<Int>>,
    title: String,
    items: List<String>,
    icon: @Composable (() -> Unit)? = null,
    confirmButton: String,
    useSelectedValuesAsSubtitle: Boolean = true,
    summary: String? = null,
    onItemsSelected: ((List<String>) -> Unit)? = null,
) {
    if (state.value.any { index -> index >= items.size }) {
        throw IndexOutOfBoundsException("Current indexes for $title list setting cannot be grater than items size")
    }

    var showDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val safeSummary = if (state.value.size >= 0 && useSelectedValuesAsSubtitle) {
        state.value.map { index -> items[index] }.joinToString(separator = ", ") { it }
    } else {
        summary
    }

    SettingsMenuLink(
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        title = title,
        summary = safeSummary,
        onClick = { showDialog = true },
    )

    if (!showDialog) return

    val onAdd: (Int) -> Unit = { selectedIndex ->
        val mutable = state.value.toMutableSet()
        mutable.add(selectedIndex)
        state.value = mutable
    }
    val onRemove: (Int) -> Unit = { selectedIndex ->
        val mutable = state.value.toMutableSet()
        mutable.remove(selectedIndex)
        state.value = mutable
    }

    AlertDialog(
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(scrollState),
            ) {
                if (summary != null) {
                    Text(text = summary, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.size(8.dp))
                }

                items.forEachIndexed { index, item ->
                    val isSelected by rememberUpdatedState(newValue = state.value.contains(index))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .toggleable(
                                role = Role.Checkbox,
                                value = isSelected,
                                onValueChange = {
                                    if (isSelected) {
                                        onRemove(index)
                                    } else {
                                        onAdd(index)
                                    }
                                },
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                        )
                    }
                }
            }
        },
        onDismissRequest = { showDialog = false },
        confirmButton = {
            AppTextButton(
                text = confirmButton,
                onClick = {
                    showDialog = false
                    onItemsSelected?.invoke(state.value.map { index -> items[index] })
                }
            )
        },
    )
}
