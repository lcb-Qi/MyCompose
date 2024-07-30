package com.lcb.one.ui.screen.menstruationAssistant.widget

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lcb.one.R

enum class MenstruationMenuAction {
    IMPORT, EXPORT
}

@Composable
fun MenstruationMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAction: (action: MenstruationMenuAction) -> Unit
) {
    val onMenuClick: (action: MenstruationMenuAction) -> Unit = {
        onDismiss()
        onAction(it)
    }

    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.export)) },
            onClick = { onMenuClick(MenstruationMenuAction.EXPORT) })
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.do_import)) },
            onClick = { onMenuClick(MenstruationMenuAction.IMPORT) }
        )
    }
}