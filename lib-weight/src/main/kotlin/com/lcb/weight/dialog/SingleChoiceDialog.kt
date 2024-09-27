package com.lcb.weight.dialog

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lcb.weight.AppTextButton
import com.lcb.weight.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val scope = rememberCoroutineScope()

    var selectedIndex by remember { mutableIntStateOf(selected) }
    val handleSelected: (Int) -> Unit = {
        selectedIndex = it
        onItemSelected(it)
        scope.launch {
            delay(100)
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
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
                                onClick = { handleSelected(index) }
                            )
                        },
                        text = { Text(text = options[index]) },
                        onClick = { handleSelected(index) }
                    )
                }
            }
        }
    )
}