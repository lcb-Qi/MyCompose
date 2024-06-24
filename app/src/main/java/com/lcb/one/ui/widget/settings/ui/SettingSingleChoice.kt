package com.lcb.one.ui.widget.settings.ui

import androidx.annotation.IntRange
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lcb.one.ui.widget.dialog.SingleChoiceDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingSingleChoice(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0)
    selectIndex: Int = 0,
    title: String,
    options: Array<String>,
    icon: (@Composable () -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    onItemSelected: ((Int) -> Unit)? = null,
) {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val delayHide: () -> Unit = {
        scope.launch {
            delay(50)
            showDialog = false
        }
    }

    SimpleSettingsMenuLink(
        colors = colors,
        modifier = modifier.minSettingsHeight(),
        enabled = enabled,
        icon = icon,
        title = title,
        summary = options[selectIndex],
        onClick = { showDialog = true }
    )

    SingleChoiceDialog(
        show = showDialog,
        title = title,
        selected = selectIndex,
        options = options,
        onItemSelected = {
            onItemSelected?.invoke(it)
            delayHide()
        },
        onDismiss = { showDialog = false }
    )
}
