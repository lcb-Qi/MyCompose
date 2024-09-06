package com.lcb.one.ui.widget.settings.ui

import androidx.annotation.IntRange
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lcb.one.R
import com.lcb.one.ui.widget.dialog.SingleChoiceDialog

@Composable
fun SettingSingleChoice(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0)
    selectIndex: Int = 0,
    title: String,
    options: Array<String>,
    icon: (@Composable () -> Unit)? = null,
    colors: ListItemColors = SettingsDefaults.colors(),
    onItemSelected: ((Int) -> Unit)? = null,
) {
    var showDialog by remember { mutableStateOf(false) }

    SimpleSettingsMenuLink(
        colors = colors,
        modifier = modifier.minSettingsHeight(),
        enabled = enabled,
        icon = icon,
        title = title,
        summary = options[selectIndex],
        onClick = { showDialog = true },
        action = { Icon(painterResource(R.drawable.ic_unfold_more), null) }
    )

    SingleChoiceDialog(
        show = showDialog,
        title = title,
        selected = selectIndex,
        options = options,
        onItemSelected = { onItemSelected?.invoke(it) },
        onDismiss = { showDialog = false }
    )
}
