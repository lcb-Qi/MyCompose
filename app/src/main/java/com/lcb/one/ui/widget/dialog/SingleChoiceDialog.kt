package com.lcb.one.ui.widget.dialog

import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.widget.common.AppTextButton

@Composable
fun SingleChoiceDialog(
    show: Boolean,
    title: String,
    options: Array<String>,
    selected: Int = 0,
    onItemSelected: (Int) -> Unit = {},
    onDismiss: () -> Unit
) {
    if (!show) return

    var selectedIndex by remember { mutableIntStateOf(selected) }
    val update: (Int) -> Unit = { selectedIndex = it }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            AppTextButton(
                text = stringResource(R.string.ok),
                onClick = { onItemSelected(selectedIndex) }
            )
        },
        dismissButton = {
            AppTextButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss
            )
        },
        title = { Text(text = title) },
        text = {
            LazyColumn(modifier = Modifier.requiredHeightIn(max = 240.dp)) {
                items(options.size, key = { it }) { index ->
                    DropdownMenuItem(
                        trailingIcon = {
                            RadioButton(
                                selected = selectedIndex == index,
                                onClick = { update(index) }
                            )
                        },
                        text = { Text(text = options[index]) },
                        onClick = { update(index) }
                    )
                }
            }
        }
    )
}