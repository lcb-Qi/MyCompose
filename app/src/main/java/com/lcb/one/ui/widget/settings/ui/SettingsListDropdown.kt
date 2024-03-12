package com.lcb.one.ui.widget.settings.ui

import androidx.annotation.IntRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.widget.settings.ui.internal.SettingsScaffold
import com.lcb.one.ui.theme.labelMedium

@Composable
fun SettingsListDropdown(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0)
    selectIndex: Int,
    title: @Composable () -> Unit,
    items: List<String>,
    icon: (@Composable () -> Unit)? = null,
    summary: (@Composable () -> Unit)? = null,
    onItemSelected: ((Int, String) -> Unit)? = null,
    menuItem: (@Composable (index: Int, text: String) -> Unit)? = null,
) {
    if (selectIndex > items.size) {
        throw IndexOutOfBoundsException("Current value of state for list setting cannot be grater than items size")
    }
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { expanded = true },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsScaffold(
            enabled = enabled,
            title = title,
            summary = summary,
            icon = icon,
            action = {
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .verticalScroll(scrollState),
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = items[selectIndex], style = labelMedium())
                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        items.forEachIndexed { index, text ->
                            DropdownMenuItem(
                                text = {
                                    if (menuItem != null) {
                                        menuItem(index, text)
                                    } else {
                                        Text(text = text)
                                    }
                                },
                                onClick = {
                                    onItemSelected?.invoke(index, text)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            },
        )
    }
}
