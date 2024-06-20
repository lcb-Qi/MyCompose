package com.lcb.one.ui.screen.applist.widget

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun AppTypeSelector(
    expanded: Boolean,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    selectedType: AppType,
    onSelected: (AppType) -> Unit,
    onDismiss: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        offset = offset,
        onDismissRequest = onDismiss,
        content = {
            AppType.entries.forEach {
                DropdownMenuItem(
                    trailingIcon = {
                        RadioButton(
                            selected = selectedType == it,
                            onClick = { onSelected(it) }
                        )
                    },
                    text = { Text(text = it.toDisplayName()) },
                    onClick = { onSelected(it) }
                )
            }
        }
    )
}